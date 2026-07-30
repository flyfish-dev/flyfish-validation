package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.CurrencyCode;
import dev.flyfish.validation.support.StandardChecks;

/** {@link CurrencyCode} 的无状态验证器。 */
public final class CurrencyCodeValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<CurrencyCode, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isCurrencyCode(value);
    }
}
