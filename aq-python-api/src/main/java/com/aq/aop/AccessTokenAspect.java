package com.aq.aop;


import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.aq.core.constant.CacheConfigEnum;
import com.aq.core.constant.CachePrefixEnum;
import com.aq.core.rediscache.ICacheService;
import com.aq.facade.vo.manage.ManageInfoVO;
import com.aq.util.other.TokenUtil;
import com.aq.util.result.RespCode;
import com.aq.util.result.ResultUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: AccessTokenAspect
 * @Description: 校验登录
 * @author: lijie
 * @date: 2018年2月10日 下午4:51:48
 */
@Slf4j
@Aspect
@Component
@Order(1)
public class AccessTokenAspect {

	@Autowired
	@SuppressWarnings("rawtypes")
    private ICacheService cacheService;

	/**
	 * 
	* @Title: validateAccessToken  
	* @Description:   
	* @param @param pjp
	* @param @param at
	* @param @return
	* @param @throws Throwable    参数  
	* @return Object    返回类型  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	@Around("@annotation(at)")
	public Object validateAccessToken(ProceedingJoinPoint pjp, AccessToken at) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String token = request.getHeader(CachePrefixEnum.ACCESSTOKEN.getCode());
		log.info("获取登录信息入参token={}", token);
		ManageInfoVO info = null;
		if (StringUtils.isNotBlank(token)) {
			info = (ManageInfoVO) cacheService.get(token);
		}
		log.info("缓存获取登录数据返回={}", JSON.toJSONString(info));
		if (null != info) {
			// 更新用户缓存活动时间
			cacheService.expire(token, CacheConfigEnum.USER_CACHE_TIME.getDuration(),
					CacheConfigEnum.USER_CACHE_TIME.getUnit());
			cacheService.expire(TokenUtil.getManagerKey(info.getTelphone()),
					CacheConfigEnum.USER_CACHE_TIME.getDuration(), CacheConfigEnum.USER_CACHE_TIME.getUnit());
			return pjp.proceed();
		} else {
			return ResultUtil.getResult(RespCode.Code.NOT_LOGIN);
		}
	}
}
