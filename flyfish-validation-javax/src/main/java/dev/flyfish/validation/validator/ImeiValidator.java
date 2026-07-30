package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.Imei;
import dev.flyfish.validation.support.StandardChecks;

/** {@link Imei} 的无状态验证器。 */
public final class ImeiValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<Imei, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isImei(value);
    }
}
