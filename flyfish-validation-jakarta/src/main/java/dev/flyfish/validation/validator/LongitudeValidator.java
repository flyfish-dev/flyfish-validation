package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.Longitude;
import dev.flyfish.validation.support.StandardChecks;

/** {@link Longitude} 的无状态验证器。 */
public final class LongitudeValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<Longitude, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isLongitude(value);
    }
}
