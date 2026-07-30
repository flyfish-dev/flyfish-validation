package dev.flyfish.validation.validator.cross;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.cross.RequiredIf;
import dev.flyfish.validation.support.BeanPropertyAccess.PropertyValue;

/** 验证条件字段命中指定值时，目标字段必须为非空值。 */
public final class RequiredIfValidator extends CrossFieldValidatorSupport
        implements ConstraintValidator<RequiredIf, Object> {

    private String conditionField;
    private String[] conditionValues;
    private String requiredField;
    private boolean ignoreCase;
    private String reportOn;

    @Override
    public void initialize(RequiredIf annotation) {
        conditionField = annotation.conditionField();
        conditionValues = annotation.conditionValues().clone();
        requiredField = annotation.requiredField();
        ignoreCase = annotation.ignoreCase();
        reportOn = annotation.reportOn();
    }

    @Override
    public boolean isValid(
            Object bean, ConstraintValidatorContext context) {
        if (bean == null) {
            return true;
        }
        PropertyValue condition = read(bean, conditionField);
        PropertyValue required = read(bean, requiredField);
        String errorPath = reportPath(reportOn, requiredField);
        if (!condition.isPresent() || !required.isPresent()) {
            return reject(context, errorPath);
        }
        if (!matchesCondition(condition.getValue())) {
            return true;
        }
        return !absent(required.getValue(), true)
                || reject(context, errorPath);
    }

    private boolean matchesCondition(Object actualValue) {
        String actual = String.valueOf(actualValue);
        for (String expected : conditionValues) {
            if (expected == null) {
                continue;
            }
            if (ignoreCase
                    ? expected.equalsIgnoreCase(actual)
                    : expected.equals(actual)) {
                return true;
            }
        }
        return false;
    }
}
