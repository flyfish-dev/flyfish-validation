package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.Vin;
import dev.flyfish.validation.support.StandardChecks;

/** {@link Vin} 的无状态验证器。 */
public final class VinValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<Vin, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isVin(value);
    }
}
