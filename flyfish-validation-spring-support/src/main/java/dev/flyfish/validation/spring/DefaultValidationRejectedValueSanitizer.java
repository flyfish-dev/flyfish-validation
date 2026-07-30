package dev.flyfish.validation.spring;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dev.flyfish.validation.api.ValidationError;

/**
 * 默认验证错误脱敏器。
 *
 * <p>默认丢弃 rejected value，避免把密码、令牌、证件号、银行卡号等输入
 * 直接返回给客户端。约束属性即使允许暴露，也会转换为 JSON 友好的简单值，
 * 防止 {@link Class}、注解实例或任意业务对象进入序列化链路。</p>
 */
public final class DefaultValidationRejectedValueSanitizer
    implements ValidationRejectedValueSanitizer {

    private static final int MAX_ATTRIBUTE_DEPTH = 4;
    private static final String[] SENSITIVE_PATH_FRAGMENTS = {
        "authorization", "cookie", "password", "passwd", "secret", "token",
        "apikey", "accesskey", "privatekey", "credential", "sessionid",
        "jsessionid", "otp", "cvv", "bankcard", "idcard"
    };

    private final boolean exposeRejectedValue;
    private final boolean exposeAttributes;

    public DefaultValidationRejectedValueSanitizer(
    boolean exposeRejectedValue, boolean exposeAttributes) {
        this.exposeRejectedValue = exposeRejectedValue;
        this.exposeAttributes = exposeAttributes;
    }

    @Override
    public ValidationError sanitize(ValidationError error) {
        if (error == null) {
            return null;
        }
        ValidationError.Builder builder = ValidationError.builder(
        error.getCode(), error.getMessage())
            .propertyPath(error.getPropertyPath())
            .severity(error.getSeverity())
            .validator(error.getValidator());
        /*
         * The general exposure switch is useful for harmless fields, but it must never override
         * the sensitive-field denylist. Otherwise a diagnostics setting could reflect passwords,
         * tokens, or credentials into API responses and logs without any warning at the call site.
         */
        if (exposeRejectedValue && !isSensitivePath(error.getPropertyPath())) {
            builder.rejectedValue(simpleValue(error.getRejectedValue(), 0));
        }
        if (exposeAttributes) {
            for (Map.Entry<String, Object> entry
                : error.getAttributes().entrySet()) {
                builder.attribute(entry.getKey(),
                simpleValue(entry.getValue(), 0));
            }
        }
        return builder.build();
    }

    private static boolean isSensitivePath(String propertyPath) {
        if (propertyPath == null || propertyPath.isEmpty()) {
            return false;
        }
        String normalized = propertyPath.toLowerCase(Locale.ROOT)
            .replace("_", "")
            .replace("-", "")
            .replace(".", "")
            .replace("[", "")
            .replace("]", "");
        for (String fragment : SENSITIVE_PATH_FRAGMENTS) {
            if (normalized.contains(fragment)) {
                return true;
            }
        }
        return false;
    }

    private static Object simpleValue(Object value, int depth) {
        if (value == null || value instanceof String
            || value instanceof Number || value instanceof Boolean
            || value instanceof Character) {
            return value;
        }
        if (value instanceof Enum<?>) {
            return ((Enum<?>) value).name();
        }
        if (value instanceof Class<?>) {
            return ((Class<?>) value).getName();
        }
        if (depth >= MAX_ATTRIBUTE_DEPTH) {
            return value.getClass().getName();
        }
        Class<?> type = value.getClass();
        if (type.isArray()) {
            int length = Array.getLength(value);
            List<Object> values = new ArrayList<Object>(length);
            for (int index = 0; index < length; index++) {
                values.add(simpleValue(Array.get(value, index), depth + 1));
            }
            return values;
        }
        if (value instanceof Collection<?>) {
            List<Object> values = new ArrayList<Object>();
            for (Object item : (Collection<?>) value) {
                values.add(simpleValue(item, depth + 1));
            }
            return values;
        }
        if (value instanceof Map<?, ?>) {
            Map<String, Object> values = new LinkedHashMap<String, Object>();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                values.put(String.valueOf(entry.getKey()),
                simpleValue(entry.getValue(), depth + 1));
            }
            return values;
        }
        return type.getName();
    }
}
