package dev.flyfish.validation.validator.cross;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.cross.DateOrder;
import dev.flyfish.validation.model.ComparisonOperator;
import dev.flyfish.validation.support.BeanPropertyAccess.PropertyValue;
import dev.flyfish.validation.support.ComparisonSupport;

/** 验证开始日期早于或等于结束日期。 */
public final class DateOrderValidator extends CrossFieldValidatorSupport
        implements ConstraintValidator<DateOrder, Object> {

    private String start;
    private String end;
    private String pattern;
    private boolean allowEqual;
    private String reportOn;

    @Override
    public void initialize(DateOrder annotation) {
        start = annotation.start();
        end = annotation.end();
        pattern = annotation.pattern();
        allowEqual = annotation.allowEqual();
        reportOn = annotation.reportOn();
    }

    @Override
    public boolean isValid(
            Object bean, ConstraintValidatorContext context) {
        if (bean == null) {
            return true;
        }
        PropertyValue startValue = read(bean, start);
        PropertyValue endValue = read(bean, end);
        String errorPath = reportPath(reportOn, end);
        if (!startValue.isPresent() || !endValue.isPresent()) {
            return reject(context, errorPath);
        }
        if (startValue.getValue() == null || endValue.getValue() == null) {
            return true;
        }
        try {
            ComparisonOperator operator = allowEqual
                    ? ComparisonOperator.LESS_THAN_OR_EQUAL
                    : ComparisonOperator.LESS_THAN;
            return ComparisonSupport.test(
                    startValue.getValue(), endValue.getValue(),
                    operator, pattern, clock(context).getZone())
                    || reject(context, errorPath);
        } catch (RuntimeException exception) {
            return reject(context, errorPath);
        }
    }
}
