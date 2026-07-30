package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.MimeType;
import dev.flyfish.validation.support.StandardChecks;

/** {@link MimeType} 的无状态验证器。 */
public final class MimeTypeValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<MimeType, CharSequence> {
    private String[] allowed;

    @Override
    public void initialize(MimeType annotation) {
        allowed = annotation.value().clone();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isMimeType(value, allowed);
    }
}
