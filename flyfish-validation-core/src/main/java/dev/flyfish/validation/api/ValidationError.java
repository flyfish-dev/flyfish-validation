package dev.flyfish.validation.api;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 与 Bean Validation、Spring Validation 和业务验证器无关的统一错误模型。
 *
 * <p>对象不可变，可安全地跨线程传递。默认 Web 处理器不会序列化
 * {@link #getRejectedValue()}，从源头降低密码、令牌和证件号泄露风险。</p>
 */
public final class ValidationError {
    private final String code;
    private final String message;
    private final String propertyPath;
    private final ValidationSeverity severity;
    private final String validator;
    private final Object rejectedValue;
    private final Map<String, Object> attributes;

    private ValidationError(Builder builder) {
        this.code = normalize(builder.code, "VALIDATION_FAILED");
        this.message = normalize(builder.message, "校验失败");
        this.propertyPath = builder.propertyPath == null ? "" : builder.propertyPath;
        this.severity = builder.severity == null ? ValidationSeverity.ERROR : builder.severity;
        this.validator = builder.validator == null ? "" : builder.validator;
        this.rejectedValue = builder.rejectedValue;
        this.attributes = Collections.unmodifiableMap(
        new LinkedHashMap<String, Object>(builder.attributes));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(String code, String message) {
        return new Builder().code(code).message(message);
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public String getPropertyPath() { return propertyPath; }
    public ValidationSeverity getSeverity() { return severity; }
    public String getValidator() { return validator; }
    public Object getRejectedValue() { return rejectedValue; }
    public Map<String, Object> getAttributes() { return attributes; }

    private static String normalize(String value, String fallback) {
        return value == null || value.trim().isEmpty() ? fallback : value.trim();
    }

    /** 创建统一错误的链式构建器。 */
    public static final class Builder {
        private String code;
        private String message;
        private String propertyPath = "";
        private ValidationSeverity severity = ValidationSeverity.ERROR;
        private String validator = "";
        private Object rejectedValue;
        private final Map<String, Object> attributes =
        new LinkedHashMap<String, Object>();

        private Builder() { }
        public Builder code(String value) { this.code = value; return this; }
        public Builder message(String value) { this.message = value; return this; }
        public Builder propertyPath(String value) { this.propertyPath = value; return this; }
        public Builder severity(ValidationSeverity value) { this.severity = value; return this; }
        public Builder validator(String value) { this.validator = value; return this; }
        public Builder rejectedValue(Object value) { this.rejectedValue = value; return this; }
        public Builder attribute(String name, Object value) {
            if (name != null && !name.trim().isEmpty()) {
                attributes.put(name.trim(), value);
            }
            return this;
        }
        public Builder attributes(Map<String, ?> values) {
            if (values != null) {
                for (Map.Entry<String, ?> entry : values.entrySet()) {
                    attribute(entry.getKey(), entry.getValue());
                }
            }
            return this;
        }
        public ValidationError build() { return new ValidationError(this); }
    }
}
