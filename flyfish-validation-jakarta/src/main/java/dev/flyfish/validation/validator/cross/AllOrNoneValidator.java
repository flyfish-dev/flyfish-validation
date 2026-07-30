package dev.flyfish.validation.validator.cross;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.cross.AllOrNone;
import dev.flyfish.validation.support.BeanPropertyAccess.PropertyValue;

/** 验证一组字段必须全部有值或全部无值。 */
public final class AllOrNoneValidator extends CrossFieldValidatorSupport
        implements ConstraintValidator<AllOrNone, Object> {

    private String[] fields;
    private boolean blankAsNull;
    private String reportOn;

    @Override
    public void initialize(AllOrNone annotation) {
        fields = annotation.fields().clone();
        blankAsNull = annotation.blankAsNull();
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
            if (!absent(value.getValue(), blankAsNull)) {
                presentCount++;
            }
        }
        return presentCount == 0 || presentCount == fields.length
                || reject(context, reportOn);
    }
}
