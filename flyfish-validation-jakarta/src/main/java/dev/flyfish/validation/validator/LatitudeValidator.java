package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.Latitude;
import dev.flyfish.validation.support.StandardChecks;

/** {@link Latitude} 的无状态验证器。 */
public final class LatitudeValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<Latitude, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isLatitude(value);
    }
}
