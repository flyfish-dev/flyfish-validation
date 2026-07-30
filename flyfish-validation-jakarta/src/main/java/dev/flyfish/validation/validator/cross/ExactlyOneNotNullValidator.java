package dev.flyfish.validation.validator.cross;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.cross.ExactlyOneNotNull;
import dev.flyfish.validation.support.BeanPropertyAccess.PropertyValue;

/** 验证指定字段中恰好存在一个非 null 值。 */
public final class ExactlyOneNotNullValidator
        extends CrossFieldValidatorSupport
        implements ConstraintValidator<ExactlyOneNotNull, Object> {

    private String[] fields;
    private String reportOn;

    @Override
    public void initialize(ExactlyOneNotNull annotation) {
        fields = annotation.fields().clone();
        reportOn = annotation.reportOn();
    }

    @Override
    public boolean isValid(
            Object bean, ConstraintValidatorContext context) {
        if (bean == null) {
            return true;
        }
        int presentCount = 0;
        for (String field : fields) {
            PropertyValue value = read(bean, field);
            if (!value.isPresent()) {
                return reject(context, reportOn);
            }
            if (value.getValue() != null && ++presentCount > 1) {
                return reject(context, reportOn);
            }
        }
        return presentCount == 1 || reject(context, reportOn);
    }
}
