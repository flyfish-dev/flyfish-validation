package dev.flyfish.validation.validator.cross;

import java.time.ZoneId;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.cross.CompareFields;
import dev.flyfish.validation.model.ComparisonOperator;
import dev.flyfish.validation.support.BeanPropertyAccess.PropertyValue;
import dev.flyfish.validation.support.ComparisonSupport;

/** 验证两个字段满足指定的通用比较关系。 */
public final class CompareFieldsValidator
        extends CrossFieldValidatorSupport
        implements ConstraintValidator<CompareFields, Object> {

    private String left;
    private String right;
    private ComparisonOperator operator;
    private String pattern;
    private String reportOn;

    @Override
    public void initialize(CompareFields annotation) {
        left = annotation.left();
        right = annotation.right();
        operator = annotation.operator();
        pattern = annotation.pattern();
        reportOn = annotation.reportOn();
    }

    @Override
    public boolean isValid(
            Object bean, ConstraintValidatorContext context) {
        if (bean == null) {
            return true;
        }
        PropertyValue leftValue = read(bean, left);
        PropertyValue rightValue = read(bean, right);
        String errorPath = reportPath(reportOn, right);
        if (!leftValue.isPresent() || !rightValue.isPresent()) {
            return reject(context, errorPath);
        }
        try {
            ZoneId zoneId = clock(context).getZone();
            return ComparisonSupport.test(
                    leftValue.getValue(), rightValue.getValue(),
                    operator, pattern, zoneId)
                    || reject(context, errorPath);
        } catch (RuntimeException exception) {
            return reject(context, errorPath);
        }
    }
}
