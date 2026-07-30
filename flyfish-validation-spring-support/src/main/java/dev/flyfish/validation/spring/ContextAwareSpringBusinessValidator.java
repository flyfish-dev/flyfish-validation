package dev.flyfish.validation.spring;

import org.springframework.validation.Errors;

import dev.flyfish.validation.business.BusinessValidationContext;

/** 需要租户、时钟或调用属性时使用的 Spring 业务验证器扩展。 */
public interface ContextAwareSpringBusinessValidator
    extends SpringBusinessValidator {
    void validate(Object target, Errors errors,
    BusinessValidationContext context);
    @Override default void validate(Object target, Errors errors) {
        validate(target, errors,
        BusinessValidationContext.builder(key()).build());
    }
}
