package dev.flyfish.validation.business;

/** 同步和异步业务验证器共享的最小元数据。 */
public interface BusinessRule<T> {
    String key();
    Class<T> targetType();
    default int order() { return 0; }
}
