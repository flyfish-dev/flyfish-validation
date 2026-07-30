package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.ByteLength;
import dev.flyfish.validation.support.StandardChecks;

/** {@link ByteLength} 的无状态验证器。 */
public final class ByteLengthValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<ByteLength, CharSequence> {
    private int min; private int max;

    @Override
    public void initialize(ByteLength annotation) {
        min = annotation.min(); max = annotation.max();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isByteLength(value, min, max);
    }
}
