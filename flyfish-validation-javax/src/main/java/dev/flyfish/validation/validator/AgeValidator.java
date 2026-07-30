package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.Age;
import dev.flyfish.validation.support.StandardChecks;

/** {@link Age} 的无状态验证器。 */
public final class AgeValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<Age, Object> {
    private int min;
    private int max;

    @Override
    public void initialize(Age annotation) {
        min = annotation.min();
        max = annotation.max();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isAge(value, min, max);
    }
}
