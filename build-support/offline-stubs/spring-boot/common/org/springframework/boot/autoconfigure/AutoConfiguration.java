package org.springframework.boot.autoconfigure;
import java.lang.annotation.*;
@Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME)
public @interface AutoConfiguration {
 Class<?>[] before() default {}; Class<?>[] after() default {};
 String[] beforeName() default {}; String[] afterName() default {};
}
