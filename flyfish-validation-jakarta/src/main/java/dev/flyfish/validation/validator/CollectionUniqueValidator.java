package dev.flyfish.validation.validator;

import java.util.Collection;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.CollectionUnique;
import dev.flyfish.validation.support.StandardChecks;

/** 验证集合元素在指定空值策略下保持唯一。 */
public final class CollectionUniqueValidator
        implements ConstraintValidator<CollectionUnique, Collection<?>> {

    private boolean ignoreNull;

    @Override
    public void initialize(CollectionUnique annotation) {
        ignoreNull = annotation.ignoreNull();
    }

    @Override
    public boolean isValid(
            Collection<?> value, ConstraintValidatorContext context) {
        return value == null
                || StandardChecks.hasUniqueElements(value, ignoreNull);
    }
}
