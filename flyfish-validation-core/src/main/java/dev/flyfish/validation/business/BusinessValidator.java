package dev.flyfish.validation.business;

import dev.flyfish.validation.api.ValidationReport;

/** 可构造器注入 Repository、Mapper 或缓存的同步业务验证器。 */
public interface BusinessValidator<T> extends BusinessRule<T> {
    ValidationReport validate(T value, BusinessValidationContext context);
}
