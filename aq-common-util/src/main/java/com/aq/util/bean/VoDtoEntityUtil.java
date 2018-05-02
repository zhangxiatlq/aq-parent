package com.aq.util.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @version 1.0.0
 * @describe ： VoDtoEntity转换工具操作类
 * @author ： 熊克文
 * @createTime： 2017/5/14
 * @Copyright by xkw
 */
@Slf4j
public class VoDtoEntityUtil extends BeanUtils {

    private VoDtoEntityUtil() {
        throw new NullPointerException();
    }

    /**
     * 将来源source对象的相同属性值通过set方法返回到新对象target
     */
    public static <T> T convert(Object source, Class<T> targetClass) {
        return convert(source, targetClass, (String[]) null);
    }

    /**
     * @param ignoreProperties 排除字段
     * 将来源source对象的相同属性值通过set方法返回到新对象target
     */
    public static <T> T convert(Object source, Class<T> targetClass, String... ignoreProperties) {
        try {
            T target = targetClass.newInstance();
            copyProperties(source, target,ignoreProperties);
            return target;
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("反射实例化对象失败", e.getCause());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将来源source对象不为空的字段的相同属性值通过set方法返回到新对象target
     */
    public static void copyPropertiesNotNull(Object source, Object target) throws BeansException {
        Class<?> actualEditable = source.getClass();
        PropertyDescriptor[] sourcePds = getPropertyDescriptors(actualEditable);
        List<String> ignoreList = new ArrayList<>();
        for (PropertyDescriptor sourcePd : sourcePds) {
            if (sourcePd != null) {
                Method readMethod = sourcePd.getReadMethod();
                if (readMethod != null) {
                    try {
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source);
                        if (Objects.isNull(value)) {
                            String readMethodName = readMethod.getName();
                            ignoreList.add(readMethodName.substring(3, 4).toLowerCase() + readMethodName.substring(4));
                        }
                    } catch (Throwable var15) {
                        throw new FatalBeanException("Could not copy property '" + sourcePd.getName() + "' from source to source", var15);
                    }
                }
            }
        }
        copyProperties(source, target, ignoreList.toArray(new String[ignoreList.size()]));
    }

    /**
     * 将来源List<source>转换成targetClass类的List<targetClass>
     */
    public static <T> List<T> convertList(List<?> list, Class<T> targetClass) {
        return list.stream().map(o -> convert(o, targetClass)).collect(Collectors.toList());
    }
}
