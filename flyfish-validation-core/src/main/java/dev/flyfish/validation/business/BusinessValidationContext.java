package dev.flyfish.validation.business;

import java.time.Clock;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 单条业务规则的不可变调用上下文。
 *
 * <p>上下文用于传递租户、区域、时钟、调用参数和调用链属性，避免验证器
 * 依赖 ThreadLocal 或静态 Holder。</p>
 */
public final class BusinessValidationContext {

    private final String rule;
    private final Locale locale;
    private final Clock clock;
    private final Map<String, String> parameters;
    private final Map<String, Object> attributes;

    private BusinessValidationContext(Builder builder) {
        rule = builder.rule;
        locale = builder.locale;
        clock = builder.clock;
        parameters = Collections.unmodifiableMap(
                new LinkedHashMap<String, String>(builder.parameters));
        attributes = Collections.unmodifiableMap(
                new LinkedHashMap<String, Object>(builder.attributes));
    }

    public static Builder builder(String rule) {
        return new Builder(rule);
    }

    public String getRule() {
        return rule;
    }

    public Locale getLocale() {
        return locale;
    }

    public Clock getClock() {
        return clock;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String parameter(String name) {
        return parameters.get(name);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public Object attribute(String name) {
        return attributes.get(name);
    }

    /** 构建单条规则调用上下文。 */
    public static final class Builder {

        private final String rule;
        private Locale locale = Locale.getDefault();
        private Clock clock = Clock.systemDefaultZone();
        private final Map<String, String> parameters =
                new LinkedHashMap<String, String>();
        private final Map<String, Object> attributes =
                new LinkedHashMap<String, Object>();

        private Builder(String rule) {
            if (rule == null || rule.trim().isEmpty()) {
                throw new IllegalArgumentException("rule 不能为空");
            }
            this.rule = rule.trim();
        }

        public Builder locale(Locale value) {
            locale = value == null ? Locale.getDefault() : value;
            return this;
        }

        public Builder clock(Clock value) {
            clock = value == null ? Clock.systemDefaultZone() : value;
            return this;
        }

        public Builder parameter(String name, String value) {
            String key = normalizeName(name);
            if (key != null) {
                parameters.put(key, value);
            }
            return this;
        }

        public Builder parameters(Map<String, String> values) {
            if (values != null) {
                for (Map.Entry<String, String> entry : values.entrySet()) {
                    parameter(entry.getKey(), entry.getValue());
                }
            }
            return this;
        }

        public Builder attribute(String name, Object value) {
            String key = normalizeName(name);
            if (key != null) {
                attributes.put(key, value);
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

        public BusinessValidationContext build() {
            return new BusinessValidationContext(this);
        }

        private static String normalizeName(String value) {
            if (value == null || value.trim().isEmpty()) {
                return null;
            }
            return value.trim();
        }
    }
}
