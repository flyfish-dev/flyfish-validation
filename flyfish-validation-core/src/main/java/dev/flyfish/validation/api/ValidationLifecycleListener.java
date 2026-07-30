package dev.flyfish.validation.api;

/**
 * 验证生命周期监听器。实现应保持轻量、线程安全，并避免记录原始敏感值。
 */
public interface ValidationLifecycleListener {
    default int order() { return 0; }
    default void beforeValidation(ValidationInvocation invocation) { }
    default void afterSuccess(ValidationInvocation invocation,
    ValidationReport report, long elapsedNanos) { }
    default void afterFailure(ValidationInvocation invocation,
    ValidationReport report, long elapsedNanos) { }
    default void afterException(ValidationInvocation invocation,
    RuntimeException exception, long elapsedNanos) { }
}
