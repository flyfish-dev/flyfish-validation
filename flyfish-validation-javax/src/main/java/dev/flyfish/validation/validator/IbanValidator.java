package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.Iban;
import dev.flyfish.validation.support.StandardChecks;

/** {@link Iban} 的无状态验证器。 */
public final class IbanValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<Iban, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isIban(value);
    }
}
