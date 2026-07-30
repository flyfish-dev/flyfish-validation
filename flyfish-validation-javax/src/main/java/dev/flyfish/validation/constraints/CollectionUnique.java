package dev.flyfish.validation.constraints;

import java.lang.annotation.*;

import javax.validation.Constraint;
import javax.validation.Payload;

import dev.flyfish.validation.validator.CollectionUniqueValidator;

/** 集合元素不重复。空值默认有效。 */
@Documented @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME) @Constraint(validatedBy = CollectionUniqueValidator.class)
public @interface CollectionUnique {
    String message() default "{dev.flyfish.validation.constraints.CollectionUnique.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean ignoreNull() default false;
}
