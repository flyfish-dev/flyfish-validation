package dev.flyfish.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import dev.flyfish.validation.model.PhoneType;
import dev.flyfish.validation.validator.PhoneValidator;

/** 可配置的电话号码。空值默认有效，必填请组合标准约束。 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
    ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
public @interface Phone {
    String message() default "{dev.flyfish.validation.constraints.Phone.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    PhoneType type() default PhoneType.ANY;
}
