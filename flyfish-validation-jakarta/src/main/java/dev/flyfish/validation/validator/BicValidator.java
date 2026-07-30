package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.Bic;
import dev.flyfish.validation.support.StandardChecks;

/** {@link Bic} 的无状态验证器。 */
public final class BicValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<Bic, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isBic(value);
    }
}
