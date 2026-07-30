package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.AllowedValues;
import dev.flyfish.validation.support.StandardChecks;

/** 验证输入值属于配置的业务白名单。 */
public final class AllowedValuesValidator
        implements ConstraintValidator<AllowedValues, Object> {

    private String[] values;
    private boolean ignoreCase;

    @Override
    public void initialize(AllowedValues annotation) {
        values = annotation.value().clone();
        ignoreCase = annotation.ignoreCase();
    }

    @Override
    public boolean isValid(
            Object value, ConstraintValidatorContext context) {
        return value == null
                || StandardChecks.isAllowed(value, values, ignoreCase);
    }
}
