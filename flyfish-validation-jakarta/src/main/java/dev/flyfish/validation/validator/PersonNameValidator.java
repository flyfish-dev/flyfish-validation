package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.PersonName;
import dev.flyfish.validation.model.NameType;
import dev.flyfish.validation.support.StandardChecks;

/** {@link PersonName} 的无状态验证器。 */
public final class PersonNameValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<PersonName, CharSequence> {
    private NameType type;
    private int min;
    private int max;

    @Override
    public void initialize(PersonName annotation) {
        type = annotation.type();
        min = annotation.min();
        max = annotation.max();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isPersonName(value, type, min, max);
    }
}
