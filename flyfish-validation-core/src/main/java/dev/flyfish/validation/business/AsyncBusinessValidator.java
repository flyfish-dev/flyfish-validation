package dev.flyfish.validation.business;

import java.util.concurrent.CompletionStage;

import dev.flyfish.validation.api.ValidationReport;

/** 不阻塞调用线程的异步业务验证器。 */
public interface AsyncBusinessValidator<T> extends BusinessRule<T> {
    CompletionStage<ValidationReport> validateAsync(
    T value, BusinessValidationContext context);
}
