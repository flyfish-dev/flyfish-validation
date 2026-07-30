package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.InternationalName;
import dev.flyfish.validation.model.NameType;
import dev.flyfish.validation.support.StandardChecks;

/** {@link InternationalName} 的无状态验证器。 */
public final class InternationalNameValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<InternationalName, CharSequence> {
    private int min;
    private int max;

    @Override
    public void initialize(InternationalName annotation) {
        min = annotation.min();
        max = annotation.max();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isPersonName(value, NameType.INTERNATIONAL, min, max);
    }
}
