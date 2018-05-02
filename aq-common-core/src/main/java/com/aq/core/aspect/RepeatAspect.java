package com.aq.core.aspect;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.aq.core.annotation.RepeatToken;
import com.aq.core.exception.BizException;
import com.aq.util.result.RespCode;
import com.aq.util.result.ResultUtil;
/**
 * 
* @ClassName: RepeatAspect 
* @Description: 重复提交控制
* @author lijie
* @date 2017年10月31日 下午2:43:53 
*
 */
@Aspect
@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
@Order(10)
@Slf4j
public class RepeatAspect {

	@Pointcut("@annotation(com.aq.core.annotation.RepeatToken)")
	public void requestChecTrack() {}
	/**
	 * 防止重复提交缓存
	 */
	private final ConcurrentMap<String, String> signMap = new ConcurrentHashMap<String, String>(64);
	/**
	 * 
	* @Title: toRequestCheck 
	* @Description:用于防止重复提交请求控制
	* @param @param point
	* @param @return
	* @param @throws Exception    设定文件 
	* @return Object    返回类型 
	* @throws
	 */
	@Around("requestChecTrack()")
	public Object toRequestCheck(ProceedingJoinPoint point) throws Exception {
		// 当前方法tokenKey值
		final String tokenKey = getRequestTokenByParam(point);
		if (log.isInfoEnabled()) {
			log.info("用于防止重复提交请求控制 ,tokenKey={}", tokenKey);
		}
		if (StringUtils.isBlank(tokenKey)) {
			return ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
		}
		// 当前方法签名
		final String methodSign = getMethodSign(point, tokenKey);
		if (null != signMap.putIfAbsent(methodSign, tokenKey)) {
			return ResultUtil.getResult(RespCode.Code.REPETITION);
		}
		try {
			// 执行方法
			return point.proceed();
		} catch (Throwable e) {
			if (e instanceof BizException) {
				BizException be = (BizException) e;
				throw new BizException(be.getMsg(), be.getCode());
			}
			throw new Exception(e);
		} finally {
			// 释放当前请求的标记
			signMap.remove(methodSign, tokenKey);
		}
	}
	/**
	 * 
	* @Title: getMethodSign 
	* @Description: 得到方法签名 
	* @param @param point
	* @param @param tokenKey
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	private String getMethodSign(JoinPoint point, String tokenKey) {
		StringBuilder sbud = new StringBuilder();
		Method m = ((MethodSignature) point.getSignature()).getMethod();
		sbud.append(m.getDeclaringClass().getName()).append(m.getName());
		Class<?>[] clas = m.getParameterTypes();
		int len = clas == null ? 0 : clas.length;
		for (int i = 0; i < len; i++) {
			sbud.append(clas[i].getName());
		}
		sbud.append(tokenKey);
		return sbud.toString();
	}
	/**
	 * 
	* @Title: getRequestTokenByParam 
	* @Description:跟据key获取参数值
	* @param @param point
	* @param @param name
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String getRequestTokenByParam(JoinPoint point) {
		RepeatToken token = getRequestToken(point);
		if (null != token) {
			if (token.isHeader()) {
				HttpServletRequest request = AspectHandle.getArg(HttpServletRequest.class, point);
				if (null != request) {
					return request.getHeader(token.headerKey());
				}
			} else {
				Object obj = AspectHandle.getValueByName(point, token.key());
				if (null != obj) {
					return obj.toString();
				}
			}
		}
		return null;
	}
	/**
	 * 
	* @Title: getAppToken 
	* @Description:得到请求token注解
	* @param @param point
	* @param @return    设定文件 
	* @return RequestToken    返回类型 
	* @throws
	 */
	private static RepeatToken getRequestToken(JoinPoint point) {
		MethodSignature methodSignature = (MethodSignature) point.getSignature();
		if (null != methodSignature) {
			Method method = methodSignature.getMethod();
			if (null != method) {
				return method.getAnnotation(RepeatToken.class);
			}
		}
		return null;
	}
}
