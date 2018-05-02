package com.aq.core.base.author;

import com.aq.core.base.BaseMapper;
import com.aq.core.base.BaseValidate;
import com.aq.core.base.author.annotation.NotRepeat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Mr.Shu
 * @packageName com.aq.core.base.vaildate
 * @description
 * @date 2017/12/7 10:52
 * @Copyright @2017 by Mr.Shu
 */
public class ValidatorCenter {

    private BaseMapper mapper;
    private String _error;

    public ValidatorCenter(BaseMapper mapper) {
        this.mapper = mapper;
    }

    public String getError() {
        return _error;
    }

    public boolean checkAnnotationValidate(BaseValidate dto) {
        //获取该model的全部字段属性
        Field[] fields = dto.getClass().getDeclaredFields();
        //联合字段查询Map
        Map<String, List<Field>> fieldMap = new HashMap<>();

        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof NotRepeat) {
                    NotRepeat notRepeat = (NotRepeat) annotation;
                    if (!notRepeat.unionKey().isEmpty()) {
                        if (fieldMap.containsKey(notRepeat.unionKey())) {
                            fieldMap.get(notRepeat.unionKey()).add(field);
                        } else {
                            List<Field> addField = new ArrayList<>();
                            addField.add(field);
                            fieldMap.put(notRepeat.unionKey(), addField);
                        }
                        continue;
                    }
                }
                List<Field> addField = new ArrayList<>();
                addField.add(field);
                IRuler ruler = FiledRulers.getRuler(annotation.annotationType().getSimpleName());
                if (ruler != null && !ruler.authorize(new ValidatorPara(mapper, dto, addField))) {
                    _error = ruler.getError();
                    return false;
                }
            }
        }

        if (fieldMap != null && fieldMap.size() > 0) {
            Collection<List<Field>> lists = fieldMap.values();
            for (List<Field> fieldList : lists) {
                IRuler ruler = FiledRulers.getRuler("NotRepeat");
                if (ruler != null && !ruler.authorize(new ValidatorPara(mapper, dto, fieldList))) {
                    _error = ruler.getError();
                    return false;
                }
            }

        }
        return true;
    }

    @Data
    @AllArgsConstructor
    public class ValidatorPara {
        private BaseMapper mapper;
        private BaseValidate dto;
        private List<Field> fieldList;
    }
}
