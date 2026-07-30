package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.Luhn;
import dev.flyfish.validation.support.StandardChecks;

/** {@link Luhn} 的无状态验证器。 */
public final class LuhnValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<Luhn, CharSequence> {
    private int min;
    private int max;

    @Override
    public void initialize(Luhn annotation) {
        min = annotation.min();
        max = annotation.max();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isLuhn(value, min, max);
    }
}
