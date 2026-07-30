package dev.flyfish.validation.constraints;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import dev.flyfish.validation.validator.NoNullElementsValidator;

/** 集合不包含 null 元素。空值默认有效。 */
@Documented @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME) @Constraint(validatedBy = NoNullElementsValidator.class)
public @interface NoNullElements {
    String message() default "{dev.flyfish.validation.constraints.NoNullElements.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
