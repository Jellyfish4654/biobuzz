package com.example.jellytest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OnBotTestInfo {
    String testSetName() default "";
    String testName() default "";
    boolean manualPassFail() default false;
    int repeats() default 1;
}
