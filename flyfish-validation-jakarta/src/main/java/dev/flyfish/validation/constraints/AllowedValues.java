package dev.flyfish.validation.constraints;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import dev.flyfish.validation.validator.AllowedValuesValidator;

/** 候选值白名单。空值默认有效。 */
@Documented @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME) @Constraint(validatedBy = AllowedValuesValidator.class)
public @interface AllowedValues {
    String message() default "{dev.flyfish.validation.constraints.AllowedValues.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] value();
    boolean ignoreCase() default false;
}
