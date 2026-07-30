package dev.flyfish.validation.api;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** 验证生命周期事件的只读上下文。 */
public final class ValidationInvocation {
    private final Object target;
    private final String operation;
    private final Instant startedAt;
    private final Map<String, Object> attributes;

    public ValidationInvocation(Object target, String operation,
    Map<String, ?> attributes) {
        this.target = target;
        this.operation = operation == null ? "validate" : operation;
        this.startedAt = Instant.now();
        Map<String, Object> copy = new LinkedHashMap<String, Object>();
        if (attributes != null) { copy.putAll(attributes); }
        this.attributes = Collections.unmodifiableMap(copy);
    }

    public Object getTarget() { return target; }
    public String getOperation() { return operation; }
    public Instant getStartedAt() { return startedAt; }
    public Map<String, Object> getAttributes() { return attributes; }
}
