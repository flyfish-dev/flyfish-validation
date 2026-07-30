package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.Money;
import dev.flyfish.validation.support.StandardChecks;

/** {@link Money} 的无状态验证器。 */
public final class MoneyValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<Money, Object> {
    private String min;
    private String max;
    private int fraction;
    private int precision;
    private boolean allowNegative;

    @Override
    public void initialize(Money annotation) {
        min = annotation.min();
        max = annotation.max();
        fraction = annotation.fraction();
        precision = annotation.precision();
        allowNegative = annotation.allowNegative();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isMoney(value, min, max, fraction, precision, allowNegative);
    }
}
