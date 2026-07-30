package dev.flyfish.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.ChinaIdCard;
import dev.flyfish.validation.region.AdministrativeDivisionProvider;
import dev.flyfish.validation.region.AdministrativeDivisionProviderAware;
import dev.flyfish.validation.region.DefaultChinaAdministrativeDivisionProvider;
import dev.flyfish.validation.support.ChineseIdCardParser;

/**
 * 中国居民身份证号码验证器。
 *
 * <p>同时校验号码结构、校验位、出生日期、最大年龄和行政区划。行政区划
 * Provider 可由 Starter 注入，避免把易变化的县级代码固化在基础库中。</p>
 */
public final class ChinaIdCardValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<ChinaIdCard, CharSequence>,
AdministrativeDivisionProviderAware {

    private boolean allowLegacy15;
    private int maximumAge;
    private AdministrativeDivisionProvider provider =
    new DefaultChinaAdministrativeDivisionProvider();

    @Override
    public void initialize(ChinaIdCard annotation) {
        allowLegacy15 = annotation.allowLegacy15();
        maximumAge = annotation.maximumAge();
    }

    @Override
    public boolean isValid(CharSequence value,
    ConstraintValidatorContext context) {
        if (nullable(value)) {
            return true;
        }
        return ChineseIdCardParser.parse(value.toString(), allowLegacy15,
        clock(context), provider, maximumAge).isPresent();
    }

    @Override
    public void setAdministrativeDivisionProvider(
    AdministrativeDivisionProvider provider) {
        if (provider != null) {
            this.provider = provider;
        }
    }
}
