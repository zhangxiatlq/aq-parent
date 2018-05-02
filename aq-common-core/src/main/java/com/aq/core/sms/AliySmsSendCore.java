package com.aq.core.sms;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aq.core.constant.CacheConfigEnum;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.core.constant.SmsTypeEnum;
import com.aq.core.rediscache.ICacheService;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.aq.util.string.SmsCodeUtil;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @ClassName: AliySmsSend
 * @Description: 短信发送
 * @author: lijie
 * @date: 2018年2月9日 上午11:22:31
 */
@Slf4j
@Component
public class AliySmsSendCore {

	 //产品名称:云通信短信API产品,开发者无需替换
    static final String PRODUCT = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String DOMAIN = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String ACCESSKEYID = "LTAIrcq4jRb5EVzG";
    static final String ACCESSKEYSECRET = "LxRfVPeoDsQNeLCqQwDuC4E8gMcSLD";
    
    @SuppressWarnings("rawtypes")
	@Autowired
    private ICacheService cacheService;
    /**
     * 
     * @Title: sendSmsCode
     * @author: lijie 
     * @Description: TODO
     * @param telphone
     * @return
     * @return: boolean
     */
	@SuppressWarnings("unchecked")
	public Result sendSmsCode(final String telphone, final SmsTypeEnum type, final RoleCodeEnum role) {
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			Assert.notNull(role, "role is not exists");
			Assert.notNull(telphone, "telphone is null");
			Assert.notNull(type, "type is not exists");
			String smsCode = SmsCodeUtil.getSmsCode();
			JSONObject json = new JSONObject();
			json.put("code", smsCode);
			if (type.isCache()) {
				cacheService.set(getKey(telphone, type.getTypeCode(), role.getCode()), smsCode,
						CacheConfigEnum.SMS_MAX_TIME.getDuration(), CacheConfigEnum.SMS_MAX_TIME.getUnit());
			}
			// 发短信
			SendSmsResponse response = sendSms(telphone, type.getModelId(), json.toJSONString());
			log.info("短信发送响应数据={}", JSON.toJSONString(response));
			if (null != response) {
				if (response.getCode().equals("OK")) {
					return ResultUtil.getResult(RespCode.Code.SUCCESS);
				} else {
					log.info(response.getMessage());
					result.setMessage("短信发送失败");
				}
			}
		} catch (Exception e) {
			log.error("短信发送异常", e);
		}
		return result;
	}
	/**
	 * 
	 * @Title: checkSmsCode
	 * @author: lijie 
	 * @Description: 校验验证码
	 * @param telphone
	 * @param smsCode
	 * @return
	 * @return: boolean
	 */
	@SuppressWarnings("unchecked")
	public Result checkSmsCode(final String telphone, final String smsCode, final SmsTypeEnum type,
			final RoleCodeEnum role) {
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		Object data = cacheService.get(getKey(telphone, type.getTypeCode(), role.getCode()));
		if (null == data) {
			result.setMessage("验证码失效");
		} else if (!data.toString().equals(smsCode)) {
			result.setMessage("验证码错误");
		} else {
			ResultUtil.setResult(result, RespCode.Code.SUCCESS);
		}
		return result;
	}
	/**
	 * 
	 * @Title: getKey
	 * @author: lijie 
	 * @Description: TODO
	 * @param telphone
	 * @return
	 * @return: String
	 */
	private String getKey(final String telphone, final String type,final Byte userType) {

		return new StringBuilder("ALIYUNSMS_").append(telphone).append(type).append(userType).toString();
	}
    /**
     * 
     * @Title: sendSms
     * @author: lijie 
     * @Description: 短信发送
     * @param telphone
     * @param templateCode
     * @param jsonData
     * @return
     * @throws ClientException
     * @return: SendSmsResponse
     */
	private SendSmsResponse sendSms(final String telphone, final String templateCode, final String jsonData)
			throws ClientException {

		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");

		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESSKEYID, ACCESSKEYSECRET);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象-具体描述见控制台-文档部分内容
		SendSmsRequest request = new SendSmsRequest();
		// 必填:待发送手机号
		request.setPhoneNumbers(telphone);
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName("量化家");
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(templateCode);
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		request.setTemplateParam(jsonData);

		// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");

		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		// request.setOutId("yourOutId");
		return acsClient.getAcsResponse(request);
	}

    /**
     * 
     * @Title: querySendDetails
     * @author: lijie 
     * @Description: 查询短信信息
     * @param bizId
     * @param telphone
     * @return
     * @throws ClientException
     * @return: QuerySendDetailsResponse
     */
	public QuerySendDetailsResponse querySendDetails(final String bizId, final String telphone) throws ClientException {

		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");

		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESSKEYID, ACCESSKEYSECRET);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象
		QuerySendDetailsRequest request = new QuerySendDetailsRequest();
		// 必填-号码
		request.setPhoneNumber(telphone);
		// 可选-流水号
		request.setBizId(bizId);
		// 必填-发送日期 支持30天内记录查询，格式yyyyMMdd
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
		request.setSendDate(ft.format(new Date()));
		// 必填-页大小
		request.setPageSize(10L);
		// 必填-当前页码从1开始计数
		request.setCurrentPage(1L);
		// hint 此处可能会抛出异常，注意catch
		return acsClient.getAcsResponse(request);
	}

}
