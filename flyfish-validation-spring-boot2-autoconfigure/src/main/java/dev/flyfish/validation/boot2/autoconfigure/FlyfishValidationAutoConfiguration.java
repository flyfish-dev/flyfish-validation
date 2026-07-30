package dev.flyfish.validation.boot2.autoconfigure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import dev.flyfish.validation.FlyfishValidator;
import dev.flyfish.validation.api.ValidationLifecycleListener;
import dev.flyfish.validation.business.AsyncBusinessValidator;
import dev.flyfish.validation.business.BusinessValidationExecutor;
import dev.flyfish.validation.business.BusinessValidator;
import dev.flyfish.validation.business.BusinessValidatorRegistry;
import dev.flyfish.validation.business.DefaultBusinessValidatorRegistry;
import dev.flyfish.validation.region.AdministrativeDivisionProvider;
import dev.flyfish.validation.region.DefaultChinaAdministrativeDivisionProvider;
import dev.flyfish.validation.spring.DefaultValidationFailureHandler;
import dev.flyfish.validation.spring.DefaultValidationRejectedValueSanitizer;
import dev.flyfish.validation.spring.FlyfishValidationProperties;
import dev.flyfish.validation.spring.SpringBusinessValidator;
import dev.flyfish.validation.spring.SpringBusinessValidatorAdapter;
import dev.flyfish.validation.spring.ValidationErrorCustomizer;
import dev.flyfish.validation.spring.ValidationFailureHandler;
import dev.flyfish.validation.spring.ValidationFailurePipeline;
import dev.flyfish.validation.spring.ValidationLifecycleDispatcher;
import dev.flyfish.validation.spring.ValidationRejectedValueSanitizer;

/** Spring Boot 2 的 Flyfish Validation 基础自动装配。 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(name =
"org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration")
@ConditionalOnClass(Validator.class)
@ConditionalOnProperty(prefix = "flyfish.validation", name = "enabled",
havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(FlyfishValidationConfigurationProperties.class)
public class FlyfishValidationAutoConfiguration {
    @Bean @ConditionalOnMissingBean
    public BusinessValidatorRegistry flyfishBusinessValidatorRegistry(
    ObjectProvider<BusinessValidator<?>> synchronous,
    ObjectProvider<AsyncBusinessValidator<?>> asynchronous,
    ObjectProvider<SpringBusinessValidator> springValidators,
    FlyfishValidationProperties properties) {
        List<BusinessValidator<?>> sync = new ArrayList<BusinessValidator<?>>(synchronous.orderedStream().collect(Collectors.toList()));
        List<AsyncBusinessValidator<?>> async = asynchronous.orderedStream().collect(Collectors.toList());
        for (SpringBusinessValidator candidate : springValidators.orderedStream().collect(Collectors.toList())) {
            if (!(candidate instanceof BusinessValidator<?>)) {
                sync.add(new SpringBusinessValidatorAdapter(candidate));
            }
        }
        return new DefaultBusinessValidatorRegistry(sync, async,
        properties.isAllowDuplicateBusinessRules());
    }

    @Bean @ConditionalOnMissingBean
    public ValidationLifecycleDispatcher flyfishValidationLifecycleDispatcher(
    ObjectProvider<ValidationLifecycleListener> listeners,
    FlyfishValidationProperties properties) {
        return new ValidationLifecycleDispatcher(
        listeners.orderedStream().collect(Collectors.toList()),
        properties.isPropagateListenerException());
    }

    @Bean @ConditionalOnMissingBean
    public BusinessValidationExecutor flyfishBusinessValidationExecutor(
    BusinessValidatorRegistry registry,
    ValidationLifecycleDispatcher dispatcher) {
        return new BusinessValidationExecutor(registry, dispatcher.listener());
    }

    @Bean @ConditionalOnMissingBean
    public AdministrativeDivisionProvider flyfishAdministrativeDivisionProvider() {
        return new DefaultChinaAdministrativeDivisionProvider();
    }

    @Bean @ConditionalOnMissingBean
    public FlyfishValidator flyfishValidator(Validator validator,
    BusinessValidatorRegistry registry,
    ValidationLifecycleDispatcher dispatcher) {
        return new FlyfishValidator(validator, registry, dispatcher.listener());
    }

    @Bean(name = "defaultValidator")
    @ConditionalOnMissingBean(Validator.class)
    public LocalValidatorFactoryBean flyfishDefaultValidator(
    AutowireCapableBeanFactory beanFactory,
    AdministrativeDivisionProvider divisionProvider,
    FlyfishValidationProperties properties) {
        LocalValidatorFactoryBean factory =
        new LocalValidatorFactoryBean();
        factory.setConstraintValidatorFactory(
        new FlyfishConstraintValidatorFactory(beanFactory, divisionProvider));
        factory.setValidationPropertyMap(Collections.singletonMap(
        "hibernate.validator.fail_fast",
        Boolean.toString(properties.isFailFast())));
        return factory;
    }

    @Bean @ConditionalOnMissingBean
    public ValidationRejectedValueSanitizer flyfishValidationRejectedValueSanitizer(
    FlyfishValidationProperties properties) {
        return new DefaultValidationRejectedValueSanitizer(
        properties.isExposeRejectedValue(),
        properties.isExposeConstraintAttributes());
    }

    @Bean @ConditionalOnMissingBean
    public ValidationFailureHandler flyfishValidationFailureHandler(
    FlyfishValidationProperties properties) {
        return new DefaultValidationFailureHandler(properties);
    }

    @Bean @ConditionalOnMissingBean
    public ValidationFailurePipeline flyfishValidationFailurePipeline(
    ValidationRejectedValueSanitizer sanitizer,
    ObjectProvider<ValidationErrorCustomizer> customizers,
    ValidationFailureHandler handler) {
        return new ValidationFailurePipeline(sanitizer,
        customizers.orderedStream().collect(Collectors.toList()), handler);
    }
}
