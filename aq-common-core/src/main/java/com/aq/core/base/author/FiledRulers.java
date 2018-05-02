package com.aq.core.base.author;

import com.aq.core.base.BaseValidate;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.List;

/**
 * @author Mr.Shu
 * @packageName com.aq.core.base.author
 * @description
 * @date 2017/12/8 15:16
 * @Copyright @2017 by Mr.Shu
 */
@Slf4j
public final class FiledRulers {
    private static Hashtable<String, IRuler> _rules = new Hashtable();
    private static FiledRulers _instance;
    public static String packageName;

    static {
        packageName = packageName == null ? FiledRulers.class.getPackage()
                .getName() : packageName;
        _instance = new FiledRulers();
    }


    private FiledRulers() {
        addRuler(new NotRepeat());
    }

    private void addRuler(IRuler ruler) {
        _rules.put(ruler.getClass().getSimpleName(), ruler);
    }

    @SuppressWarnings("unchecked")
    public static IRuler getRuler(String name) {
        if (_rules.containsKey(name)) {
            return _rules.get(name);
        }
        try {
            Class<FieldValidate> c = (Class<FieldValidate>) Class.forName(packageName
                    + "." + name);
            IRuler r = c.newInstance();
            _instance.addRuler(r);
            return r;
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }
        return null;
    }

    public final class NotRepeat extends FieldValidate {
        @Override
        public Boolean authorize(ValidatorCenter.ValidatorPara validatorPara) {
            BaseValidate dto = validatorPara.getDto();
            Object entity;
            if (super.authorize(validatorPara)) {
                List<Field> fieldList = validatorPara.getFieldList();
                if (fieldList != null && fieldList.size() > 0) {
                    entity = getEntity();
                    for (Field field : fieldList) {
                        Object fieldValue = getEntityClazzFieldValue(field.getName(), dto);
                        setEntityClazzFieldValue(field.getName(), (Class<?>) field.getGenericType(), fieldValue, entity);
                        _errMsg = field.getAnnotation(com.aq.core.base.author.annotation.NotRepeat.class).message();
                    }

                    List list = mapper.select(entity);
                    if (list != null && list.size() > 0) {
                        Object id = getEntityClazzFieldValue("id", dto);
                        if (id != null) {
                            for (Object o : list) {
                                Object entId = getEntityClazzFieldValue("id", o);
                                if (id.equals(entId)) {
                                    return true;
                                }
                            }

                        }
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    }
}
