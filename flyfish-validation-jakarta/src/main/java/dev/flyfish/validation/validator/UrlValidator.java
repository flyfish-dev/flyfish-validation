package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.Url;
import dev.flyfish.validation.support.StandardChecks;

/** {@link Url} 的无状态验证器。 */
public final class UrlValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<Url, CharSequence> {
    private String[] schemes;
    private boolean requireHost;

    @Override
    public void initialize(Url annotation) {
        schemes = annotation.schemes().clone();
        requireHost = annotation.requireHost();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isUrl(value, schemes, requireHost);
    }
}
