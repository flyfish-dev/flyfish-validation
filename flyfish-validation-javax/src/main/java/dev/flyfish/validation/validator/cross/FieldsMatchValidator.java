package dev.flyfish.validation.validator.cross;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.cross.FieldsMatch;
import dev.flyfish.validation.support.BeanPropertyAccess.PropertyValue;

/** 验证两个字段的值相等，包含两个字段同时为 null 的场景。 */
public final class FieldsMatchValidator extends CrossFieldValidatorSupport
        implements ConstraintValidator<FieldsMatch, Object> {

    private String first;
    private String second;
    private String reportOn;

    @Override
    public void initialize(FieldsMatch annotation) {
        first = annotation.first();
        second = annotation.second();
        reportOn = annotation.reportOn();
    }

    @Override
    public boolean isValid(
            Object bean, ConstraintValidatorContext context) {
        if (bean == null) {
            return true;
        }
        PropertyValue left = read(bean, first);
        PropertyValue right = read(bean, second);
        if (!left.isPresent() || !right.isPresent()) {
            return reject(context, reportPath(reportOn, second));
        }
        return Objects.equals(left.getValue(), right.getValue())
                || reject(context, reportPath(reportOn, second));
    }
}
