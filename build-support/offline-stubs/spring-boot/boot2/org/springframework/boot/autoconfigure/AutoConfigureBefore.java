package org.springframework.boot.autoconfigure;
import java.lang.annotation.*;
@Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME)
public @interface AutoConfigureBefore {
    Class<?>[] value() default {};
    String[] name() default {};
}
