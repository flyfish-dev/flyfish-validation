package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.MacAddress;
import dev.flyfish.validation.support.StandardChecks;

/** {@link MacAddress} 的无状态验证器。 */
public final class MacAddressValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<MacAddress, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isMacAddress(value);
    }
}
