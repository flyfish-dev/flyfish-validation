package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.Username;
import dev.flyfish.validation.support.StandardChecks;

/** {@link Username} 的无状态验证器。 */
public final class UsernameValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<Username, CharSequence> {
    private int min;
    private int max;

    @Override
    public void initialize(Username annotation) {
        min = annotation.min();
        max = annotation.max();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isUsername(value, min, max);
    }
}
