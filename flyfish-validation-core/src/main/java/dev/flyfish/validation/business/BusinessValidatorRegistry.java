package dev.flyfish.validation.business;

import java.util.Set;

/** 应用启动阶段形成的不可变业务规则注册表。 */
public interface BusinessValidatorRegistry {
    boolean contains(String key);
    boolean containsSynchronous(String key);
    boolean containsAsynchronous(String key);
    Set<String> keys();
    BusinessValidator<?> synchronous(String key);
    AsyncBusinessValidator<?> asynchronous(String key);
}
