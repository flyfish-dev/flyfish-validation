package org.springframework.boot.context.properties;
import java.lang.annotation.*;
@Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME)
public @interface ConfigurationProperties { String value() default ""; String prefix() default ""; boolean ignoreInvalidFields() default false; boolean ignoreUnknownFields() default true; }
