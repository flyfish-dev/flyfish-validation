package dev.flyfish.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import dev.flyfish.validation.validator.Base64ValueValidator;

/** Base64 编码文本。空值默认有效，必填请组合标准约束。 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
    ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Base64ValueValidator.class)
public @interface Base64Value {
    String message() default "{dev.flyfish.validation.constraints.Base64Value.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean urlSafe() default false;
}
