package dev.flyfish.validation.validator.cross;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.cross.ExactlyOneNotBlank;
import dev.flyfish.validation.support.BeanPropertyAccess.PropertyValue;

/** 验证指定字段中恰好存在一个非空文本值。 */
public final class ExactlyOneNotBlankValidator
        extends CrossFieldValidatorSupport
        implements ConstraintValidator<ExactlyOneNotBlank, Object> {

    private String[] fields;
    private String reportOn;

    @Override
    public void initialize(ExactlyOneNotBlank annotation) {
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
            if (!absent(value.getValue(), true) && ++presentCount > 1) {
                return reject(context, reportOn);
            }
        }
        return presentCount == 1 || reject(context, reportOn);
    }
}
