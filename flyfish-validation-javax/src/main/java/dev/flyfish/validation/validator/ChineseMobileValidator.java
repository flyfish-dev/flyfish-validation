package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.ChineseMobile;
import dev.flyfish.validation.model.PhoneType;
import dev.flyfish.validation.support.StandardChecks;

/** {@link ChineseMobile} 的无状态验证器。 */
public final class ChineseMobileValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<ChineseMobile, CharSequence> {
    private boolean allowCountryCode;
    private boolean allowSeparators;

    @Override
    public void initialize(ChineseMobile annotation) {
        allowCountryCode = annotation.allowCountryCode();
        allowSeparators = annotation.allowSeparators();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        String text = value.toString().trim();
        if (!allowCountryCode && (text.startsWith("+86") || text.startsWith("0086"))) { return false; }
        if (!allowSeparators && (text.contains(" ") || text.contains("-") || text.contains("(") || text.contains(")"))) { return false; }
        return StandardChecks.isPhone(value, PhoneType.CHINA_MOBILE);
    }
}
