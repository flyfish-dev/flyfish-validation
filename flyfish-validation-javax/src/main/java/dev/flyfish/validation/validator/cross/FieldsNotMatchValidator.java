package dev.flyfish.validation.validator.cross;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.cross.FieldsNotMatch;
import dev.flyfish.validation.support.BeanPropertyAccess.PropertyValue;

/** 验证两个字段的值不相等。 */
public final class FieldsNotMatchValidator
        extends CrossFieldValidatorSupport
        implements ConstraintValidator<FieldsNotMatch, Object> {

    private String first;
    private String second;
    private String reportOn;

    @Override
    public void initialize(FieldsNotMatch annotation) {
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
        return !Objects.equals(left.getValue(), right.getValue())
                || reject(context, reportPath(reportOn, second));
    }
}
