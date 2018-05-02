package com.aq.core.annotation;

import com.aq.core.base.BaseEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @version 2.1
 * @项目：orion-parent
 * @描述： description
 * @作者： 熊克文
 * @创建时间： 2017/8/1
 * @Copyright by xkw
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityClass {
    Class<? extends BaseEntity> value();

}
