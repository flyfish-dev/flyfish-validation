package dev.flyfish.validation.boot3.autoconfigure;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorFactory;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import dev.flyfish.validation.region.AdministrativeDivisionProvider;
import dev.flyfish.validation.region.AdministrativeDivisionProviderAware;

/**
 * 同时支持 Spring 依赖注入与 Flyfish 基础设施注入的验证器工厂。
 *
 * <p>用户自定义 {@link ConstraintValidator} 可以直接使用 Spring 构造器注入；
 * 对实现 {@link AdministrativeDivisionProviderAware} 的内置验证器，本工厂还会
 * 注入应用定义的行政区划 Provider。</p>
 */
final class FlyfishConstraintValidatorFactory
    implements ConstraintValidatorFactory {

    private final SpringConstraintValidatorFactory delegate;
    private final AdministrativeDivisionProvider divisionProvider;

    FlyfishConstraintValidatorFactory(AutowireCapableBeanFactory beanFactory,
    AdministrativeDivisionProvider divisionProvider) {
        delegate = new SpringConstraintValidatorFactory(beanFactory);
        this.divisionProvider = divisionProvider;
    }

    @Override
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        T validator = delegate.getInstance(key);
        if (validator instanceof AdministrativeDivisionProviderAware) {
            ((AdministrativeDivisionProviderAware) validator)
                .setAdministrativeDivisionProvider(divisionProvider);
        }
        return validator;
    }

    @Override
    public void releaseInstance(ConstraintValidator<?, ?> instance) {
        delegate.releaseInstance(instance);
    }
}
