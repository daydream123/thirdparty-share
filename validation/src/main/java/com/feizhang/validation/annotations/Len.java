package com.feizhang.validation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Len {
    int min();

    int max();

    String nullMessage() default "%s不能为null";

    String lenMessage() default "%s长度不在范围%d和%d之间";
}
