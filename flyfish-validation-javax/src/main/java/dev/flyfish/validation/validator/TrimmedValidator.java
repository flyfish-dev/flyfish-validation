package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.Trimmed;
import dev.flyfish.validation.support.StandardChecks;

/** {@link Trimmed} 的无状态验证器。 */
public final class TrimmedValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<Trimmed, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isTrimmed(value);
    }
}
