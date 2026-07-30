package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.ChinaLandline;
import dev.flyfish.validation.model.PhoneType;
import dev.flyfish.validation.support.StandardChecks;

/** {@link ChinaLandline} 的无状态验证器。 */
public final class ChinaLandlineValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<ChinaLandline, CharSequence> {
    private boolean allowCountryCode;
    private boolean allowExtension;

    @Override
    public void initialize(ChinaLandline annotation) {
        allowCountryCode = annotation.allowCountryCode();
        allowExtension = annotation.allowExtension();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        String text = value.toString().trim();
        if (!allowCountryCode && (text.startsWith("+86") || text.startsWith("0086"))) { return false; }
        if (!allowExtension && (text.contains("转") || text.toLowerCase().contains("ext"))) { return false; }
        return StandardChecks.isPhone(value, PhoneType.CHINA_LANDLINE);
    }
}
