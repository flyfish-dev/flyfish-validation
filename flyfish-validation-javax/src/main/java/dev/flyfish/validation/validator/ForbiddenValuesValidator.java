package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.ForbiddenValues;
import dev.flyfish.validation.support.StandardChecks;

/** 验证输入值不属于配置的业务黑名单。 */
public final class ForbiddenValuesValidator
        implements ConstraintValidator<ForbiddenValues, Object> {

    private String[] values;
    private boolean ignoreCase;

    @Override
    public void initialize(ForbiddenValues annotation) {
        values = annotation.value().clone();
        ignoreCase = annotation.ignoreCase();
    }

    @Override
    public boolean isValid(
            Object value, ConstraintValidatorContext context) {
        return value == null
                || StandardChecks.isForbidden(value, values, ignoreCase);
    }
}
