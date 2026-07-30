package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.Uuid;
import dev.flyfish.validation.support.StandardChecks;

/** {@link Uuid} 的无状态验证器。 */
public final class UuidValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<Uuid, CharSequence> {
    private boolean allowCompact;

    @Override
    public void initialize(Uuid annotation) {
        allowCompact = annotation.allowCompact();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isUuid(value, allowCompact);
    }
}
