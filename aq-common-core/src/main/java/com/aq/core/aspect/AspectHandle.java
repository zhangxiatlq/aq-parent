package com.aq.core.aspect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.ui.Model;

import com.aq.util.container.ClassUtil;

/**
 * 
 * @ClassName:  AspectUtil   
 * @Description:解析切面方法参数   
 * @author: 李杰
 * @date:   2017年5月30日 下午4:47:30     
 * @Copyright: 2017
 */
public class AspectHandle {
	
	private AspectHandle(){
		
	}
	
	private static final LocalVariableTableParameterNameDiscoverer P_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();
	/**
	 * 参数类型
	 */
	private static final String[] TYPES = { 
		"java.lang.Integer", 
		"java.lang.Double", 
		"java.lang.Float",
		"java.lang.Long", 
		"java.lang.Short", 
		"java.lang.Byte", 
		"java.lang.Boolean", 
		"java.lang.Character",
		"java.lang.String", 
		"int", 
		"double", 
		"long", 
		"short", 
		"byte", 
		"boolean", 
		"char", 
		"float" 
	};
	/**
	 * 需要过滤的请求参数
	 */
	private static final Class<?>[] FILTERS =  {
		ServletRequest.class,
		HttpSession.class,
		Model.class,
		ServletResponse.class
	};
	/**
	 * 加载时执行排序
	 */
	static {
		Arrays.sort(TYPES);
	}
	 /**
	  * 
	  * @Title: getValueByName   
	  * @Description: 根据名字得到具体的参数值   
	  * @param: @param point
	  * @param: @param name
	  * @param: @return      
	  * @return: Object
	  * @author：李杰      
	  * @throws
	  */
	public static Object getValueByName(JoinPoint point, String name) {
		Object result = null;
		if (StringUtils.isBlank(name)) {
			return result;
		}
		Object[] objects = point.getArgs();
		if (ArrayUtils.isNotEmpty(objects)) {
			int num = valueNum(point, name);
			if (num >= 0) {
				result = objects[num];
			} else {
				objects = getParams(point.getArgs());
				if (ArrayUtils.isNotEmpty(objects)) {
					int len = objects.length;
					for (int i = 0; i < len; i++) {
						result = ClassUtil.getPropertiesValue(name, objects[i]);
						if (null != result) {
							break;
						}
					}
				}
			}
		}
		return result;
	}
	
	private static Object[] getParams(Object[] objs) {
		int num = objs == null ? 0 : objs.length;
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < num; i++) {
			if (!isFilter(objs[i])) {
				list.add(objs[i]);
			}
		}
		return list.toArray();
	}
	/**
	 * 
	* @Title: isFilter 
	* @Description: 校验是否存在过滤的数据中 
	* @param @param obj
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	private static boolean isFilter(Object obj) {
		for (Class<?> clas : FILTERS) {
			if (clas.isInstance(obj)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 
	 * @Title: valueNum   
	 * @Description: 得到基本数据类型参数的下标   
	 * @param: @param point
	 * @param: @param name
	 * @param: @return      
	 * @return: int
	 * @author：李杰      
	 * @throws
	 */
	private static int valueNum(JoinPoint point, String name) {
		int num = -1;
		if (StringUtils.isNotBlank(name)) {
			MethodSignature methodSignature = (MethodSignature) point.getSignature();
			if (null != methodSignature) {
				Method method = methodSignature.getMethod();
				Class<?>[] parameterTypes = method.getParameterTypes();
				int len = parameterTypes == null ? 0 : parameterTypes.length;
				for (int i = 0; i < len; i++) {
					if (isExistsType(parameterTypes[i].getName()) && isExistsParamName(method, name)) {
						num = i;
						break;
					}
				}
			}
		}
		return num;
	}
	/**
	 * 
	 * @Title: isExistsType   
	 * @Description: 判断参数是否属于基本数据类型   
	 * @param: @param typeName
	 * @param: @return      
	 * @return: boolean
	 * @author：李杰      
	 * @throws
	 */
	private static boolean isExistsType(String typeName) {

		return Arrays.binarySearch(TYPES, typeName) >= 0;
	}
	/**
	 * 
	 * @Title: getParameterNames   
	 * @Description: 得到所有的方法参数签名   
	 * @param: @param method
	 * @param: @return      
	 * @return: String[]
	 * @author：李杰      
	 * @throws
	 */
	public static String[] getParameterNames(Method method) {

		return P_NAME_DISCOVERER.getParameterNames(method);
	}
	/**
	 * 
	 * @Title: isExistsParamName   
	 * @Description: 判断字段名称是否该方法  
	 * @param: @param method
	 * @param: @param name
	 * @param: @return      
	 * @return: boolean
	 * @author：李杰      
	 * @throws
	 */
	public static boolean isExistsParamName(Method method, String name) {
		if (null != name) {
			String[] params = getParameterNames(method);
			int len = params == null ? 0 : params.length;
			for (int i = 0; i < len; i++) {
				if (name.equals(params[i])) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	* @Title: getArg 
	* @Description: 得到请求参数 
	* @param @param clas
	* @param @param point
	* @param @return    设定文件 
	* @return T    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getArg(Class<T> clas, JoinPoint point) {
		T t = null;
		Object[] objects = point.getArgs();
		if (ArrayUtils.isNotEmpty(objects)) {
			for (Object object : objects) {
				if (clas.isInstance(object)) {
					t = (T) object;
					break;
				}
			}
		}
		return t;
	}
}
