package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.IpAddress;
import dev.flyfish.validation.model.IpVersion;
import dev.flyfish.validation.support.StandardChecks;

/** {@link IpAddress} 的无状态验证器。 */
public final class IpAddressValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<IpAddress, CharSequence> {
    private IpVersion version;

    @Override
    public void initialize(IpAddress annotation) {
        version = annotation.version();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isIpAddress(value, version);
    }
}
