package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.ChineseOrEnglish;
import dev.flyfish.validation.support.StandardChecks;

/** {@link ChineseOrEnglish} 的无状态验证器。 */
public final class ChineseOrEnglishValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<ChineseOrEnglish, CharSequence> {
    private boolean whitespace; private boolean digits; private boolean punctuation;

    @Override
    public void initialize(ChineseOrEnglish annotation) {
        whitespace = annotation.allowWhitespace(); digits = annotation.allowDigits(); punctuation = annotation.allowPunctuation();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isChineseOrEnglish(value, whitespace, digits, punctuation);
    }
}
