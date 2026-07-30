package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.ChinaLicensePlate;
import dev.flyfish.validation.support.StandardChecks;

/** {@link ChinaLicensePlate} 的无状态验证器。 */
public final class ChinaLicensePlateValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<ChinaLicensePlate, CharSequence> {
    private boolean includeNewEnergy;

    @Override
    public void initialize(ChinaLicensePlate annotation) {
        includeNewEnergy = annotation.includeNewEnergy();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isChinaLicensePlate(value, includeNewEnergy);
    }
}
