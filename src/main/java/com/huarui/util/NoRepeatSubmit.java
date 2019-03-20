package com.huarui.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @功能描述 防止重复提交标记注解
 * @author www.gaozz.club
 * @date 2018-08-26
 */
@Target(ElementType.METHOD) // 作用到方法上
@Retention(RetentionPolicy.RUNTIME)// 运行时有效
public @interface NoRepeatSubmit {

} 