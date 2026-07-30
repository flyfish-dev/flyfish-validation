package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.EnumValue;
import dev.flyfish.validation.support.StandardChecks;

/** 验证输入值能够映射为指定枚举的名称。 */
public final class EnumValueValidator
        implements ConstraintValidator<EnumValue, Object> {

    private Class<? extends Enum<?>> type;
    private boolean ignoreCase;

    @Override
    public void initialize(EnumValue annotation) {
        type = annotation.value();
        ignoreCase = annotation.ignoreCase();
    }

    @Override
    public boolean isValid(
            Object value, ConstraintValidatorContext context) {
        return value == null
                || StandardChecks.isEnumValue(value, type, ignoreCase);
    }
}
