package dev.flyfish.validation.constraints;

import java.lang.annotation.*;

import javax.validation.Constraint;
import javax.validation.Payload;

import dev.flyfish.validation.validator.ForbiddenValuesValidator;

/** 候选值黑名单。空值默认有效。 */
@Documented @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME) @Constraint(validatedBy = ForbiddenValuesValidator.class)
public @interface ForbiddenValues {
    String message() default "{dev.flyfish.validation.constraints.ForbiddenValues.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] value();
    boolean ignoreCase() default false;
}
