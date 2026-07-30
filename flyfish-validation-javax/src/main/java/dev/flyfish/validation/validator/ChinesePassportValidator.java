package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.ChinesePassport;
import dev.flyfish.validation.support.StandardChecks;

/** {@link ChinesePassport} 的无状态验证器。 */
public final class ChinesePassportValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<ChinesePassport, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isChinesePassport(value);
    }
}
