package org.springframework.boot.context.properties;
import java.lang.annotation.*;
@Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME)
public @interface EnableConfigurationProperties { Class<?>[] value() default {}; }
