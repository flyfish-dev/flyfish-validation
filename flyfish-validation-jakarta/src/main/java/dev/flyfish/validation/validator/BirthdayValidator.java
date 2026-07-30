package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.Birthday;
import dev.flyfish.validation.support.StandardChecks;

/** {@link Birthday} 的无状态验证器。 */
public final class BirthdayValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<Birthday, CharSequence> {
    private String pattern;
    private String zone;
    private int minAge;
    private int maxAge;

    @Override
    public void initialize(Birthday annotation) {
        pattern = annotation.pattern();
        zone = annotation.zone();
        minAge = annotation.minAge();
        maxAge = annotation.maxAge();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isBirthday(value, pattern, zone, minAge, maxAge, clock(context));
    }
}
