package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.EnglishCharacters;
import dev.flyfish.validation.support.StandardChecks;

/** {@link EnglishCharacters} 的无状态验证器。 */
public final class EnglishCharactersValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<EnglishCharacters, CharSequence> {
    private boolean whitespace; private boolean digits; private boolean punctuation;

    @Override
    public void initialize(EnglishCharacters annotation) {
        whitespace = annotation.allowWhitespace(); digits = annotation.allowDigits(); punctuation = annotation.allowPunctuation();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isEnglishCharacters(value, whitespace, digits, punctuation);
    }
}
