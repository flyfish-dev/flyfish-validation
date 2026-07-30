package dev.flyfish.validation.validator.cross;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.cross.AtLeastOneNotBlank;
import dev.flyfish.validation.support.BeanPropertyAccess.PropertyValue;

/** 验证指定字段中至少存在一个非空文本值。 */
public final class AtLeastOneNotBlankValidator
        extends CrossFieldValidatorSupport
        implements ConstraintValidator<AtLeastOneNotBlank, Object> {

    private String[] fields;
    private String reportOn;

    @Override
    public void initialize(AtLeastOneNotBlank annotation) {
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
            if (!absent(value.getValue(), true)) {
                return true;
            }
        }
        return reject(context, reportOn);
    }
}
