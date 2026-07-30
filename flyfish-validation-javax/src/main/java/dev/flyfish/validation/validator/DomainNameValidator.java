package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.DomainName;
import dev.flyfish.validation.support.StandardChecks;

/** {@link DomainName} 的无状态验证器。 */
public final class DomainNameValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<DomainName, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isDomainName(value);
    }
}
