package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.ChineseCharacters;
import dev.flyfish.validation.support.StandardChecks;

/** {@link ChineseCharacters} 的无状态验证器。 */
public final class ChineseCharactersValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<ChineseCharacters, CharSequence> {
    private boolean whitespace; private boolean digits; private boolean punctuation;

    @Override
    public void initialize(ChineseCharacters annotation) {
        whitespace = annotation.allowWhitespace(); digits = annotation.allowDigits(); punctuation = annotation.allowPunctuation();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isChineseCharacters(value, whitespace, digits, punctuation);
    }
}
