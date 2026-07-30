package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.HexValue;
import dev.flyfish.validation.support.StandardChecks;

/** {@link HexValue} 的无状态验证器。 */
public final class HexValueValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<HexValue, CharSequence> {
    private boolean evenLength;

    @Override
    public void initialize(HexValue annotation) {
        evenLength = annotation.evenLength();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isHex(value, evenLength);
    }
}
