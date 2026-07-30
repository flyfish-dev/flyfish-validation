package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.DatePattern;
import dev.flyfish.validation.support.StandardChecks;

/** {@link DatePattern} 的无状态验证器。 */
public final class DatePatternValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<DatePattern, CharSequence> {
    private String pattern;

    @Override
    public void initialize(DatePattern annotation) {
        pattern = annotation.pattern();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.matchesDatePattern(value, pattern);
    }
}
