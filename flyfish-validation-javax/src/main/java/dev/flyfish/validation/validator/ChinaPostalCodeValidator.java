package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.ChinaPostalCode;
import dev.flyfish.validation.support.StandardChecks;

/** {@link ChinaPostalCode} 的无状态验证器。 */
public final class ChinaPostalCodeValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<ChinaPostalCode, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isChinaPostalCode(value);
    }
}
