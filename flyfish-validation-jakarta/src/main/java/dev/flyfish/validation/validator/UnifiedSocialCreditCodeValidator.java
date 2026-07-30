package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.UnifiedSocialCreditCode;
import dev.flyfish.validation.support.StandardChecks;

/** {@link UnifiedSocialCreditCode} 的无状态验证器。 */
public final class UnifiedSocialCreditCodeValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<UnifiedSocialCreditCode, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isUnifiedSocialCreditCode(value);
    }
}
