package org.springframework.core.annotation;
import java.lang.annotation.*;
@Target({ElementType.TYPE,ElementType.METHOD}) @Retention(RetentionPolicy.RUNTIME)
public @interface Order { int value(); }
