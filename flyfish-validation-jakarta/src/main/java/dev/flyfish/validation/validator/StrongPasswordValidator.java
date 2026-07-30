package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.StrongPassword;
import dev.flyfish.validation.support.StandardChecks;

/** {@link StrongPassword} 的无状态验证器。 */
public final class StrongPasswordValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<StrongPassword, CharSequence> {
    private int min; private int max; private boolean upper; private boolean lower;
    private boolean digit; private boolean special; private boolean rejectCommon; private int maxRepeated;

    @Override
    public void initialize(StrongPassword annotation) {
        min = annotation.min(); max = annotation.max(); upper = annotation.requireUpper();
        lower = annotation.requireLower(); digit = annotation.requireDigit(); special = annotation.requireSpecial();
        rejectCommon = annotation.rejectCommon(); maxRepeated = annotation.maxRepeated();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isStrongPassword(value, min, max, upper, lower, digit, special, rejectCommon, maxRepeated);
    }
}
