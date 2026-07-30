package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.NoWhitespace;
import dev.flyfish.validation.support.StandardChecks;

/** {@link NoWhitespace} 的无状态验证器。 */
public final class NoWhitespaceValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<NoWhitespace, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.containsNoWhitespace(value);
    }
}
