package dev.flyfish.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import dev.flyfish.validation.model.NameType;
import dev.flyfish.validation.validator.PersonNameValidator;

/** 可配置的自然人姓名。空值默认有效，必填请组合标准约束。 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
    ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PersonNameValidator.class)
public @interface PersonName {
    String message() default "{dev.flyfish.validation.constraints.PersonName.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    NameType type() default NameType.ANY;
    int min() default 2;
    int max() default 64;
}
