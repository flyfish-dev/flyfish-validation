package dev.flyfish.validation.spring;

import java.util.Collection;

import dev.flyfish.validation.api.CompositeValidationLifecycleListener;
import dev.flyfish.validation.api.ValidationLifecycleListener;

/**
 * 隔离“用户监听器集合”与最终组合监听器 Bean 类型，避免 Spring 在构造
 * 组合监听器时把当前 Bean 再次注入自身形成循环依赖。
 */
public final class ValidationLifecycleDispatcher {
    private final ValidationLifecycleListener listener;

    public ValidationLifecycleDispatcher(
    Collection<? extends ValidationLifecycleListener> listeners,
    boolean propagateListenerException) {
        this.listener = new CompositeValidationLifecycleListener(
        listeners, propagateListenerException);
    }

    public ValidationLifecycleListener listener() {
        return listener;
    }
}
