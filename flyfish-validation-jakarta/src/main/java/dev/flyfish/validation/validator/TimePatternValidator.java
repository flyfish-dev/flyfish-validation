package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.TimePattern;
import dev.flyfish.validation.support.StandardChecks;

/** {@link TimePattern} 的无状态验证器。 */
public final class TimePatternValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<TimePattern, CharSequence> {
    private String pattern;

    @Override
    public void initialize(TimePattern annotation) {
        pattern = annotation.pattern();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.matchesTimePattern(value, pattern);
    }
}
