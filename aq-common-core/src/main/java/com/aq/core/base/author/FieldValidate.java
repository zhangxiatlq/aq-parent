package com.aq.core.base.author;

import com.aq.core.base.BaseMapper;
import com.aq.core.base.BaseValidate;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Mr.Shu
 * @packageName com.aq.core.base.vaildate
 * @description
 * @date 2017/12/8 10:12
 * @Copyright @2017 by Mr.Shu
 */
public abstract class FieldValidate implements IRuler {
    protected String _errMsg;
    protected BaseMapper mapper;
    protected Class<?> entityClazz;

    @Override
    public String getError() {
        return _errMsg;
    }

    @Override
    public Boolean authorize(ValidatorCenter.ValidatorPara validatorPara) {
        if (validatorPara == null) {
            _errMsg = "验证参数错误";
            return false;
        } else if (validatorPara.getMapper() != null) {
            mapper = validatorPara.getMapper();
        } else {
            _errMsg = "验证参数错误";
            return false;
        }
        return true;
    }

    /**
     * 获取泛型实体
     *
     * @return {@link Class<?>}
     * @author Mr.Shu
     * @date 2017/12/7 16:56
     */
    protected Class<?> getEntityClazz() {

        if (entityClazz == null) {

            try {
                Type[] types = mapper.getClass().getInterfaces()[0].getGenericInterfaces();
                for (Type type : types) {
                    if (type instanceof ParameterizedType) {
                        ParameterizedType t = (ParameterizedType) type;
                        Class<?> clazz = (Class<?>) t.getActualTypeArguments()[0];
                        this.entityClazz = clazz;
                    }
                }
            } catch (ClassCastException e) {
                throw new NullPointerException("接口实现类未摄入泛型");
            }

        }
        return entityClazz;
    }

    /**
     * 获取新实体
     *
     * @return {@link Object}
     * @author Mr.Shu
     * @date 2017/12/7 16:56
     */
    protected Object getEntity() {
        try {
            return getEntityClazz().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把dto复制到实体
     *
     * @param record {@link BaseValidate}
     * @return {@link Object}
     * @author Mr.Shu
     * @date 2017/12/7 16:56
     */
    protected Object getDtoToEntity(BaseValidate record) {
        Object entity = getEntity();
        //复制Dto数据到实体
        if (entity != null) {
            BeanUtils.copyProperties(record, entity);
        }
        return entity;
    }

    /**
     * 获取dto指定字段的值
     *
     * @param fieldName {@link String}
     * @param entity    {@link Object}
     * @return {@link Object}
     * @author Mr.Shu
     * @date 2017/12/7 16:55
     */
    protected Object getEntityClazzFieldValue(String fieldName, Object entity) {
        try {
            Method getFunc = entity.getClass().getMethod("get" + getFieldName(fieldName));
            Object fieldValue = getFunc.invoke(entity);
            return fieldValue;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置指定字段的值
     *
     * @param fieldName  {@link String}
     * @param argsClazz  {@link Class}
     * @param fieldValue {@link Object}
     * @param entity     {@link Object}
     * @author Mr.Shu
     * @date 2017/12/7 16:53
     */
    protected void setEntityClazzFieldValue(String fieldName, Class<?> argsClazz, Object fieldValue, Object entity) {
        try {
            Method setFunc = entity.getClass().getMethod("set" + getFieldName(fieldName), argsClazz);
            setFunc.invoke(entity, fieldValue);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 格式化字段名
     *
     * @param fieldName {@link String}
     * @return {@link String}
     * @author Mr.Shu
     * @date 2017/12/7 16:52
     */
    protected String getFieldName(String fieldName) {
        fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return fieldName;
    }
}