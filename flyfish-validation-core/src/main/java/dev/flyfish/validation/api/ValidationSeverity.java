package dev.flyfish.validation.api;

/** 验证错误严重级别，便于业务按错误、警告和提示分别处理。 */
public enum ValidationSeverity {
    INFO,
    WARNING,
    ERROR
}
