package dev.flyfish.validation.spring;

/** 可完全替换默认 HTTP 状态与响应包络的策略接口。 */
public interface ValidationFailureHandler {
    ValidationFailureAction handle(ValidationFailure failure);
}
