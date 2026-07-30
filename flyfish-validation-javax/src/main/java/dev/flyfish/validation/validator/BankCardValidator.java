package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.BankCard;
import dev.flyfish.validation.support.StandardChecks;

/** {@link BankCard} 的无状态验证器。 */
public final class BankCardValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<BankCard, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isBankCard(value);
    }
}
