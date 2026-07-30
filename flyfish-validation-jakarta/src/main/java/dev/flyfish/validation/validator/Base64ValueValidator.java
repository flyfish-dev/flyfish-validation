package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.Base64Value;
import dev.flyfish.validation.support.StandardChecks;

/** {@link Base64Value} 的无状态验证器。 */
public final class Base64ValueValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<Base64Value, CharSequence> {
    private boolean urlSafe;

    @Override
    public void initialize(Base64Value annotation) {
        urlSafe = annotation.urlSafe();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isBase64(value, urlSafe);
    }
}
