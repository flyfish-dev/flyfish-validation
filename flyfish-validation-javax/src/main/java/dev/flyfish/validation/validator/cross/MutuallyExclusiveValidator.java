package dev.flyfish.validation.validator.cross;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.cross.MutuallyExclusive;
import dev.flyfish.validation.support.BeanPropertyAccess.PropertyValue;

/** 验证一组字段最多只能有一个有值。 */
public final class MutuallyExclusiveValidator
        extends CrossFieldValidatorSupport
        implements ConstraintValidator<MutuallyExclusive, Object> {

    private String[] fields;
    private boolean blankAsNull;
    private String reportOn;

    @Override
    public void initialize(MutuallyExclusive annotation) {
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
            if (!absent(value.getValue(), blankAsNull)
                    && ++presentCount > 1) {
                return reject(context, reportOn);
            }
        }
        return true;
    }
}
