package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.NumericValue;
import dev.flyfish.validation.support.StandardChecks;

/** {@link NumericValue} 的无状态验证器。 */
public final class NumericValueValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<NumericValue, CharSequence> {
    private boolean integerOnly;

    @Override
    public void initialize(NumericValue annotation) {
        integerOnly = annotation.integerOnly();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isNumeric(value, integerOnly);
    }
}
