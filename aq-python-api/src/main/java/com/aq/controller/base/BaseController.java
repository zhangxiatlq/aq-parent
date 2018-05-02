package com.aq.controller.base;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.core.constant.CacheConfigEnum;
import com.aq.core.constant.CachePrefixEnum;
import com.aq.core.rediscache.ICacheService;
import com.aq.facade.service.customer.ICustomerService;
import com.aq.facade.vo.customer.CustomerInfoVO;
import com.aq.facade.vo.manage.ManageInfoVO;
import com.aq.util.encrypt.RSAUtils;
import com.aq.util.other.TokenUtil;
import com.aq.util.result.Result;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: BaseController
 * @Description: 用户缓存控制
 * @author: lijie
 * @date: 2018年2月9日 下午3:09:23
 */
@Slf4j
public class BaseController {

	@Autowired
	protected ICacheService<String, Object> cacheService;
	/**
	 * 
	 * @Title: save
	 * @author: lijie 
	 * @Description: 保存用户信息
	 * @param info
	 * @return: void
	 */
	protected void save(ManageInfoVO info) {
		Assert.notNull(info, "user info is null");
		String token = TokenUtil.generateToken();
		info.setToken(token);
		cacheService.set(TokenUtil.getManagerKey(info.getTelphone()), token,
				CacheConfigEnum.USER_CACHE_TIME.getDuration(), CacheConfigEnum.USER_CACHE_TIME.getUnit());
		cacheService.set(token, info, CacheConfigEnum.USER_CACHE_TIME.getDuration(),
				CacheConfigEnum.USER_CACHE_TIME.getUnit());
	}
	/**
	 * 
	 * @Title: getManageInfo
	 * @author: lijie 
	 * @Description: 获取客户信息数据
	 * @param isThrow
	 * @return
	 * @return: ManageInfoVO
	 */
    protected ManageInfoVO getManageInfo(boolean isThrow) {
        return this.getManageInfo(((ServletRequestAttributes) RequestContextHolder.
                getRequestAttributes()).getRequest(), isThrow);
    }

    /**
     * @param isThrow
     * @return
     * @Title: getManageInfo
     * @author: lijie
     * @Description: 获取客户信息数据
     * @return: ManageInfoVO
     */
    protected ManageInfoVO getManageInfo(HttpServletRequest request, boolean isThrow) {

        ManageInfoVO result = null;
        String token = request.getHeader(CachePrefixEnum.ACCESSTOKEN.getCode());
        if (StringUtils.isNotBlank(token)) {
            result = (ManageInfoVO) cacheService.get(token);
        }
        if (isThrow) {
            Assert.notNull(result, "user info is not exists");
        }
        return result;
    }
    
    /**
	 * 
	 * @Title: decryptByPrivateKey
	 * @author: lijie 
	 * @Description: 解密
	 * @param data
	 * @return
	 * @return: String
	 */
	protected String decryptByPrivateKey(String data) {
		String result = "";
		try {
			byte[] datas = RSAUtils.decryptByPrivateKey(data.getBytes(), RSAUtils.PRIVATEKEY);
			result = Base64.encodeBase64String(datas);
			log.info("解密后数据={}", result);
			return result;
		} catch (Exception e) {
			log.error("解密异常", e);
		}
		return result;
	}
	/**
	 * 
	 * @Title: checkSign
	 * @author: lijie 
	 * @Description: 校验加密签名
	 * @param json与加密格式一样
	 * @param sign
	 * @return
	 * @return: boolean
	 */
	protected boolean checkSign(Object obj, String sign) {
		try {
			// 加密解析后的数据
			byte[] datas = RSAUtils.encryptByPublicKey(obj.toString().getBytes(), RSAUtils.PUBLICKEY, "JS");
			return RSAUtils.verify(datas, RSAUtils.PUBLICKEY, RSAUtils.sign(sign.getBytes(), RSAUtils.PRIVATEKEY));
		} catch (Exception e) {
			log.error("校验签名失败", e);
		}
		return false;
	}
	
	@Reference(version = "1.0.0", check = false)
	protected ICustomerService customerService;
	
	/**
	 *
	 * @Title: 客户数据
	 * @author: lijie
	 * @Description: 客户数据信息
	 * @return: ClientUserInfoVO
	 */
	protected CustomerInfoVO getCustomerInfo(final String openId) {
		CustomerInfoVO result = null;
		Result cresult = customerService.getCustomerByOpenId(openId);
		log.info("根据openId查询客户信息返回数据={}", JSON.toJSONString(cresult));
		Object data = cresult.getData();
		if (data instanceof CustomerInfoVO) {
			result = (CustomerInfoVO) data;
		}
		return result;
	}
}
