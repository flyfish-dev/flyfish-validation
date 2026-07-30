package dev.flyfish.validation.validator.cross;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.cross.AtLeastOneNotNull;
import dev.flyfish.validation.support.BeanPropertyAccess.PropertyValue;

/** 验证指定字段中至少存在一个非 null 值。 */
public final class AtLeastOneNotNullValidator
        extends CrossFieldValidatorSupport
        implements ConstraintValidator<AtLeastOneNotNull, Object> {

    private String[] fields;
    private String reportOn;

    @Override
    public void initialize(AtLeastOneNotNull annotation) {
        fields = annotation.fields().clone();
        reportOn = annotation.reportOn();
    }

    @Override
    public boolean isValid(
            Object bean, ConstraintValidatorContext context) {
        if (bean == null) {
            return true;
        }
        for (String field : fields) {
            PropertyValue value = read(bean, field);
            if (!value.isPresent()) {
                return reject(context, reportOn);
            }
            if (value.getValue() != null) {
                return true;
            }
        }
        return reject(context, reportOn);
    }
}
