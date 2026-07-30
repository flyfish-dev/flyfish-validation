package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.Percentage;
import dev.flyfish.validation.support.StandardChecks;

/** {@link Percentage} 的无状态验证器。 */
public final class PercentageValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<Percentage, Object> {
    private boolean includeZero;
    private boolean includeHundred;

    @Override
    public void initialize(Percentage annotation) {
        includeZero = annotation.includeZero();
        includeHundred = annotation.includeHundred();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isPercentage(value, includeZero, includeHundred);
    }
}
