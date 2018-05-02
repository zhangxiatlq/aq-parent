package com.aq.util.container;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lijie
 * @ClassName: ClassOperateUtil
 * @Description: class 操作类
 * @date 2017年3月13日
 */
@Slf4j
public final class ClassUtil {
    /**
     * 根据clas 获取字段名
     * 不获取父类
     *
     * @param clas
     * @return
     */
    public static String[] getFiledStrByClass(Class<?> clas) {
        if (clas != null) {
            Field[] fields = clas.getDeclaredFields();
            return getFiledStrByFileds(fields);
        }
        return null;
    }

    /**
     * @param @param  clas
     * @param @return 参数
     * @return String[]    返回类型
     * @throws
     * @Title: getFiledStrByClassBySuper
     * @Description: 获取所有的字段包括父类
     */
    public static String[] getFiledStrByClassBySuper(Class<?> clas) {
        String[] fields = null;
        if (clas != null) {
            List<Field> fieldList = new ArrayList<Field>();
            while (clas != null) {
                fieldList.addAll(Arrays.asList(clas.getDeclaredFields()));
                clas = clas.getSuperclass();
            }
            if (!fieldList.isEmpty()) {
                Field[] fs = new Field[fieldList.size()];
                fields = getFiledStrByFileds(fieldList.toArray(fs));
            }
        }
        return fields;
    }

    /**
     * @throws
     * @Title: getFiledStrByFileds
     * @Description: 根据字段 得到字段名
     * @param: @param fields
     * @param: @return
     * @return: String[]
     */
    public static String[] getFiledStrByFileds(Field[] fields) {
        int len;
        if (fields != null && (len = fields.length) > 0) {
            String[] strs = new String[len];
            for (int i = 0; i < len; i++) {
                strs[i] = fields[i].getName();
            }
            return strs;
        }
        return null;
    }

    /**
     * @throws
     * @Title: setPropertiesValue
     * @Description: 根据class 赋值
     * @param: @param fieldName
     * @param: @param obj
     * @param: @param objValue
     * @return: void
     */
	public static void setPropertiesValue(String fieldName, Object obj, Object objValue) {
		Assert.notNull(obj, "set object is null");
		Assert.notNull(fieldName, "set object field name is null");
		Assert.notNull(objValue, "set object obj value is null");
		try {
			Class<?> objClas = obj.getClass();
			if (isWriteMethod(objClas, fieldName, objValue)) {
				new PropertyDescriptor(fieldName, objClas).getWriteMethod().invoke(obj, objValue);
			}
		} catch (IntrospectionException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
	}

    /**
     * @throws
     * @Title: isReadMethod
     * @Description: 根据返回类型判断是否是get 方法
     * @param: @param clas
     * @param: @return
     * @return: boolean
     */
	public static boolean isReadMethod(Class<?> clas, String name) {
		Method[] ms = clas.getMethods();
		for (Method m : ms) {
			if (isRead(m, name)) {
				return true;
			}
		}
		return false;
	}
	/**
     * @throws
     * @Title: isReadType
     * @Description: 是否是读取类型
     * @param: @param m
     * @param: @return
     * @return: boolean
     */
	private static boolean isRead(Method m, String name) {
		Type type = m.getGenericReturnType();
		if (null != type && type != void.class) {
			String resource = m.getName();
			if (resource.startsWith("get")) {
				return resource.substring("get".length()).equalsIgnoreCase(name);
			}
		}
		return false;
	}
    /**
     * @throws
     * @Title: isWriteMethod
     * @Description:
     * @param: @param clas
     * @param: @param name
     * @param: @return
     * @return: boolean
     */
	private static boolean isWriteMethod(Class<?> clas, String name, Object value) {
		Method[] ms = clas.getMethods();
		for (Method m : ms) {
			if (isWrite(m, value.getClass(), name)) {
				return true;
			}
		}
		return false;
	}
	
	private static Map<String, Type> TYPES;
	
	static {
		TYPES = new HashMap<>();
		TYPES.put("java.lang.Integer", int.class);
		TYPES.put("java.lang.Double", double.class);
		TYPES.put("java.lang.Float", float.class);
		TYPES.put("java.lang.Long", long.class);
		TYPES.put("java.lang.Short", short.class);
		TYPES.put("java.lang.Byte", byte.class);
		TYPES.put("java.lang.Boolean", boolean.class);
		TYPES.put("java.lang.Character", char.class);
	}
    /**
     * @throws
     * @Title: isWrite
     * @Description: 判断设置的参数类型是否相等
     * @param: @param m
     * @param: @param type
     * @param: @return
     * @return: boolean
     */
	private static boolean isWrite(Method m, Type type, String name) {
		Class<?>[] clas = m.getParameterTypes();
		if (null != clas && clas.length == 1) {
			boolean flag = false;
			Type baseType = TYPES.get(type.getTypeName());
			if (null == baseType) {
				flag = (type == clas[0]);
			} else {
				flag = (baseType == clas[0] || type == clas[0]);
			}
			if (flag) {
				String resource = m.getName();
				if (resource.startsWith("set")) {
					return resource.substring("set".length()).equalsIgnoreCase(name);
				}
			}
		}
		return false;
	}
    /**
     * @param @param  str
     * @param @return 参数
     * @return String    返回类型
     * @throws
     * @Title: toLowerCase
     * @Description:首字母转小写
     */
    public static String toLowerCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'A' && ch[0] <= 'Z') {
            ch[0] = (char) (ch[0] + 32);
        }
        return new String(ch);
    }

    /**
     * @param @param  mth
     * @param @param  fieldsName
     * @param @param  suffix
     * @param @return 参数
     * @return boolean    返回类型
     * @throws
     * @Title: isFieldsExistsMethod
     * @Description: 判断是否存在该方法
     */
	public static boolean isFieldsExistsMethod(Method[] mth, String fieldsName, String suffix) {
		for (Method m : mth) {
			String name = m.getName();
			if (name.startsWith(suffix)) {
				name = name.substring(suffix.length());
				if (name.equalsIgnoreCase(fieldsName)) {
					return true;
				}
			}
		}
		return false;
	}

    /**
     * 根据字段名返回值
     *
     * @param fieldName
     * @param clazz
     * @param obj
     * @return
     */
	public static Object getPropertiesValue(String fieldName, Object obj) {
		Assert.notNull(obj, "get object is null");
		Assert.notNull(fieldName, "get object field name is null");
		try {
			Class<?> objClas = obj.getClass();
			if (isReadMethod(objClas, fieldName)) {
				return new PropertyDescriptor(fieldName, objClas).getReadMethod().invoke(obj);
			}
		} catch (IntrospectionException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

    /**
	 * 
	 * @Title: checkRequest
	 * @author: lijie 
	 * @Description: 校验
	 * @param datas
	 * @return
	 * @throws Exception
	 * @return: String
	 */
	public static String checkRequest(Object datas) throws Exception {
		Object data = null;
		if (null == datas) {
			return "请求参数不能为为空";
		}
		if (datas instanceof List) {
			List<?> objs = (List<?>) datas;
			for (Object obj : objs) {
				data = invoke(obj);
				if (null != data) {
					break;
				}
			}
		} else {
			data = invoke(datas);
		}
		return null == data ? null : data.toString();
	}

	private static Object invoke(Object obj) throws Exception {
		Method me = getValidateFormMethod(obj.getClass());
		if (null == me) {
			Class<?> clazz = obj.getClass().getSuperclass();
			while (null != clazz && null == me) {
				me = getValidateFormMethod(clazz);
				clazz = clazz.getSuperclass();
			}
		}
		return null == me ? null : me.invoke(obj);
	}

	private static Method getValidateFormMethod(Class<?> clazz) {
		Method me = null;
		Method[] methods = clazz.getMethods();
		for (Method m : methods) {
			if (m.getName().equals("validateForm") && m.getParameterCount() == 0) {
				me = m;
				break;
			}
		}
		return me;
	}
	
	@SuppressWarnings("rawtypes")
	private static final Map<Class, Object> CLASMAP = new HashMap<>();

	static {
		CLASMAP.put(Integer.class, 0);
		CLASMAP.put(Double.class, 0.00);
		CLASMAP.put(Short.class, 0.0);
		CLASMAP.put(String.class, "-");
		CLASMAP.put(Long.class, 0L);
		CLASMAP.put(Collection.class, new ArrayList<>());
		CLASMAP.put(Map.class, new HashMap<>());
		CLASMAP.put(Boolean.class, false);
	}
	/**
	 * 
	* @Title: setDefvalue  
	* @Description:  设置默认值 
	* @param @param obj
	* @param @param nonReplace 参数:不需要替换的字段 名
	* @return void    返回类型  
	* @throws
	 */
	public static void setDefvalue(Object obj, String... nonReplace) {
		Map<String, String> map = new HashMap<>();
		if (null != nonReplace) {
			for (String str : nonReplace) {
				map.put(str, str);
			}
		}
		for (Field field : obj.getClass().getDeclaredFields()) {
			if (null != map.get(field.getName())) {
				continue;
			}
			field.setAccessible(true);
			try {
				if (null == field.get(obj)) {
					Object value = CLASMAP.get(field.getType());
					if (null != value) {
						field.set(obj, value);
					}
				}
			} catch (Exception e) {
				log.error("object set default value", e);
			}
		}
	}
}
