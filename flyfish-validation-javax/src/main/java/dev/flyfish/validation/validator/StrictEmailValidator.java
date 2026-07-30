package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.StrictEmail;
import dev.flyfish.validation.support.StandardChecks;

/** {@link StrictEmail} 的无状态验证器。 */
public final class StrictEmailValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<StrictEmail, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isStrictEmail(value);
    }
}
