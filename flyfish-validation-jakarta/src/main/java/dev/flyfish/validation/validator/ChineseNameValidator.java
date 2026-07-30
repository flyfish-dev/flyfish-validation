package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.ChineseName;
import dev.flyfish.validation.model.NameType;
import dev.flyfish.validation.support.StandardChecks;

/** {@link ChineseName} 的无状态验证器。 */
public final class ChineseNameValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<ChineseName, CharSequence> {
    private int min;
    private int max;

    @Override
    public void initialize(ChineseName annotation) {
        min = annotation.min();
        max = annotation.max();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isPersonName(value, NameType.CHINESE, min, max);
    }
}
