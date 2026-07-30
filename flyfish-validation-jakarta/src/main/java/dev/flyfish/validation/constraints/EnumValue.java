package dev.flyfish.validation.constraints;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import dev.flyfish.validation.validator.EnumValueValidator;

/** 枚举名称。空值默认有效。 */
@Documented @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME) @Constraint(validatedBy = EnumValueValidator.class)
public @interface EnumValue {
    String message() default "{dev.flyfish.validation.constraints.EnumValue.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends Enum<?>> value();
    boolean ignoreCase() default false;
}
