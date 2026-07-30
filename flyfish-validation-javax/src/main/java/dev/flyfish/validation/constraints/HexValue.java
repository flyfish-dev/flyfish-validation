package dev.flyfish.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import dev.flyfish.validation.validator.HexValueValidator;

/** 十六进制文本。空值默认有效，必填请组合标准约束。 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
    ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HexValueValidator.class)
public @interface HexValue {
    String message() default "{dev.flyfish.validation.constraints.HexValue.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean evenLength() default false;
}
