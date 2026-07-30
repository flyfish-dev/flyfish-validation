package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.NoNullElements;
import dev.flyfish.validation.support.StandardChecks;

/** 验证可迭代对象中不包含 null 元素。 */
public final class NoNullElementsValidator
        implements ConstraintValidator<NoNullElements, Iterable<?>> {

    @Override
    public boolean isValid(
            Iterable<?> value, ConstraintValidatorContext context) {
        return value == null || StandardChecks.hasNoNullElements(value);
    }
}
