package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.OrganizationCode;
import dev.flyfish.validation.support.StandardChecks;

/** {@link OrganizationCode} 的无状态验证器。 */
public final class OrganizationCodeValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<OrganizationCode, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isOrganizationCode(value);
    }
}
