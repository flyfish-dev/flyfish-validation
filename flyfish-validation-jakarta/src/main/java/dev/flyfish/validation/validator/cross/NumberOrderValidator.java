package dev.flyfish.validation.validator.cross;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.cross.NumberOrder;
import dev.flyfish.validation.model.ComparisonOperator;
import dev.flyfish.validation.support.BeanPropertyAccess.PropertyValue;
import dev.flyfish.validation.support.ComparisonSupport;

/** 验证较小数值小于或等于较大数值。 */
public final class NumberOrderValidator extends CrossFieldValidatorSupport
        implements ConstraintValidator<NumberOrder, Object> {

    private String smaller;
    private String larger;
    private boolean allowEqual;
    private String reportOn;

    @Override
    public void initialize(NumberOrder annotation) {
        smaller = annotation.smaller();
        larger = annotation.larger();
        allowEqual = annotation.allowEqual();
        reportOn = annotation.reportOn();
    }

    @Override
    public boolean isValid(
            Object bean, ConstraintValidatorContext context) {
        if (bean == null) {
            return true;
        }
        PropertyValue smallerValue = read(bean, smaller);
        PropertyValue largerValue = read(bean, larger);
        String errorPath = reportPath(reportOn, larger);
        if (!smallerValue.isPresent() || !largerValue.isPresent()) {
            return reject(context, errorPath);
        }
        if (smallerValue.getValue() == null
                || largerValue.getValue() == null) {
            return true;
        }
        try {
            ComparisonOperator operator = allowEqual
                    ? ComparisonOperator.LESS_THAN_OR_EQUAL
                    : ComparisonOperator.LESS_THAN;
            return ComparisonSupport.test(
                    smallerValue.getValue(), largerValue.getValue(),
                    operator, "", clock(context).getZone())
                    || reject(context, errorPath);
        } catch (RuntimeException exception) {
            return reject(context, errorPath);
        }
    }
}
