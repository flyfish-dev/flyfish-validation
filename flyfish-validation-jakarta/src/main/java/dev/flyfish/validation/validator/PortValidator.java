package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.Port;
import dev.flyfish.validation.support.StandardChecks;

/** {@link Port} 的无状态验证器。 */
public final class PortValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<Port, Object> {
    private int min;
    private int max;

    @Override
    public void initialize(Port annotation) {
        min = annotation.min();
        max = annotation.max();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isPort(value, min, max);
    }
}
