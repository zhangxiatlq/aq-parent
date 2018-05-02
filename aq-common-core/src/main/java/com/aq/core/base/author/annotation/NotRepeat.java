package com.aq.core.base.author.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Mr.Shu
 * @packageName com.aq.core.annotation
 * @description 验证实体字段值与数据库不重复注解
 * @date 2017/12/7 10:52
 * @Copyright @2017 by Mr.Shu
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
public @interface NotRepeat {

    /**
     * 返回错误信息
     *
     * @return {@link String}
     * @author Mr.Shu
     * @date 2017/12/11 16:09
     */
    String message();

    /**
     * 联合验证字段的key，key相同分为同一组
     *
     * @return {@link String}
     * @author Mr.Shu
     * @date 2017/12/11 16:10
     */
    String unionKey() default "";
}
