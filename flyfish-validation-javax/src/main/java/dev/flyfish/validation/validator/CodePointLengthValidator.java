package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.CodePointLength;
import dev.flyfish.validation.support.StandardChecks;

/** {@link CodePointLength} 的无状态验证器。 */
public final class CodePointLengthValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<CodePointLength, CharSequence> {
    private int min; private int max;

    @Override
    public void initialize(CodePointLength annotation) {
        min = annotation.min(); max = annotation.max();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isCodePointLength(value, min, max);
    }
}
