package com.aq.util.container;

import com.aq.util.date.SyncDateUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @ClassName: BeanToMap
 * @Description:bean 转 map
 * @author: 李杰
 * @date: 2017年5月18日 上午10:00:49
 * @Copyright: 2017
 */
public class BeanToMap {

    private static final Logger log = LoggerFactory.getLogger(BeanToMap.class);


    /**
     * @param obj
     * @param dateMap
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author 郑朋
     * @create 2018/2/9 0009
     */
    public static Map<String, Object> beanToMapByDate(Object obj, Map<String, String> dateMap) {
        Map<String, Object> map = ContainerUtil.map();
        if (obj != null) {
            String[] fields = ClassUtil.getFiledStrByClassBySuper(obj.getClass());
            if (fields != null) {
                for (String field : fields) {
                    Object value = ClassUtil.getPropertiesValue(field, obj);
                    if (value != null) {
                        map.put(field, getValueByDateMap(field, value, dateMap));
                    }
                }
                SyncDateUtil.remove();
            }
        }
        return map;
    }

     /**
     * @Title: getValueByDateMap
     * @Description: 处理时间格式
     * @param: @param filed
     * @param: @param value
     * @param: @param dateMap
     * @param: @return
     * @return: Object
     * @author：李杰
     */
    private static Object getValueByDateMap(String filed, Object value, Map<String, String> dateMap) {
        if (dateMap != null) {
            String format = dateMap.get(filed);
            if (StringUtils.isNotBlank(format)) {
                if (value instanceof String) {
                    value = SyncDateUtil.strToDate(format, value.toString(), false);
                }
            }
        }
        return value;
    }

    /**
     * @throws
     * @Title: getMapByStr
     * @Description: 将 bean 转换成 map String
     * @param: @param bean
     * @param: @return
     * @return: map
     * @author：李杰
     */
    public static Map<String, String> getMapByStr(Object bean) {
        Map<String, String> map = null;
        try {
            if (bean != null) {
                map = BeanUtils.describe(bean);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return map;
    }
}
