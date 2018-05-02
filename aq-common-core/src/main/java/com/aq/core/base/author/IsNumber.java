package com.aq.core.base.author;

import com.aq.core.base.BaseValidate;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Mr.Shu
 * @packageName com.aq.core.base.author
 * @description
 * @date 2017/12/11 15:01
 * @Copyright @2017 by Mr.Shu
 */
public class IsNumber extends FieldValidate {
    @Override
    public Boolean authorize(ValidatorCenter.ValidatorPara validatorPara) {
        if (super.authorize(validatorPara)) {
            BaseValidate dto = validatorPara.getDto();
            List<Field> fieldList = validatorPara.getFieldList();
            if (fieldList != null && fieldList.size() > 0) {
                Field field = fieldList.get(0);
                Object v = getEntityClazzFieldValue(field.getName(), dto);
                if (v instanceof Number) {
                    return true;
                }
                _errMsg = field.getAnnotation(com.aq.core.base.author.annotation.IsNumber.class).message();
                return false;
            }
        }
        return false;
    }
}
