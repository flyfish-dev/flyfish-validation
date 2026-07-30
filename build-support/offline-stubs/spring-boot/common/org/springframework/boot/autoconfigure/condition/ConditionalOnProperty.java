package org.springframework.boot.autoconfigure.condition;
import java.lang.annotation.*;
@Target({ElementType.TYPE,ElementType.METHOD}) @Retention(RetentionPolicy.RUNTIME)
public @interface ConditionalOnProperty { String prefix() default ""; String[] name() default {}; String havingValue() default ""; boolean matchIfMissing() default false; }
