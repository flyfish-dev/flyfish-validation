package org.springframework.boot.autoconfigure.condition;
import java.lang.annotation.*;
@Target({ElementType.TYPE,ElementType.METHOD}) @Retention(RetentionPolicy.RUNTIME)
public @interface ConditionalOnMissingBean { Class<?>[] value() default {}; String[] name() default {}; }
