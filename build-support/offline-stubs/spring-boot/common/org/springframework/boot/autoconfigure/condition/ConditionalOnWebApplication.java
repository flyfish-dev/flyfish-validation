package org.springframework.boot.autoconfigure.condition;
import java.lang.annotation.*;
@Target({ElementType.TYPE,ElementType.METHOD}) @Retention(RetentionPolicy.RUNTIME)
public @interface ConditionalOnWebApplication { Type type() default Type.ANY; enum Type { ANY,SERVLET,REACTIVE } }
