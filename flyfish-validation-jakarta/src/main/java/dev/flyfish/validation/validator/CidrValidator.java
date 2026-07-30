package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.Cidr;
import dev.flyfish.validation.model.IpVersion;
import dev.flyfish.validation.support.StandardChecks;

/** {@link Cidr} 的无状态验证器。 */
public final class CidrValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<Cidr, CharSequence> {
    private IpVersion version;

    @Override
    public void initialize(Cidr annotation) {
        version = annotation.version();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isCidr(value, version);
    }
}
