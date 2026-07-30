package dev.flyfish.validation.validator;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.ChinaAdministrativeDivisionCode;
import dev.flyfish.validation.region.AdministrativeDivisionLevel;
import dev.flyfish.validation.region.AdministrativeDivisionProvider;
import dev.flyfish.validation.region.AdministrativeDivisionProviderAware;
import dev.flyfish.validation.region.DefaultChinaAdministrativeDivisionProvider;

/**
 * 中国行政区划代码验证器。
 *
 * <p>默认仅执行稳定的编码结构和省级前缀检查；在 Spring Boot 环境中，
 * Starter 会通过 {@link AdministrativeDivisionProviderAware} 注入数据库、
 * Redis 或主数据服务实现，从而完成精确且可按日期查询的区划校验。</p>
 */
public final class ChinaAdministrativeDivisionCodeValidator
    extends ConstraintValidatorSupport
    implements ConstraintValidator<ChinaAdministrativeDivisionCode, CharSequence>,
AdministrativeDivisionProviderAware {

    private AdministrativeDivisionLevel level;
    private AdministrativeDivisionProvider provider =
    new DefaultChinaAdministrativeDivisionProvider();

    @Override
    public void initialize(ChinaAdministrativeDivisionCode annotation) {
        level = annotation.level();
    }

    @Override
    public boolean isValid(CharSequence value,
    ConstraintValidatorContext context) {
        if (nullable(value)) {
            return true;
        }
        String code = value.toString().trim();
        return provider.exists(code, level, LocalDate.now(clock(context)));
    }

    @Override
    public void setAdministrativeDivisionProvider(
    AdministrativeDivisionProvider provider) {
        if (provider != null) {
            this.provider = provider;
        }
    }
}
