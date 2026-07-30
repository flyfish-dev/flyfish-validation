package dev.flyfish.validation.business;

import java.time.Clock;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 多条业务规则执行时使用的不可变策略。
 *
 * <p>调用方可以控制 fail-fast、缺失规则行为、国际化区域、业务时钟以及
 * 传递给每条规则的参数和扩展属性。构建完成后所有集合均不可修改。</p>
 */
public final class BusinessValidationOptions {

    private static final BusinessValidationOptions DEFAULTS = builder().build();

    private final boolean failFast;
    private final boolean failOnMissingRule;
    private final Locale locale;
    private final Clock clock;
    private final Map<String, String> parameters;
    private final Map<String, Object> attributes;

    private BusinessValidationOptions(Builder builder) {
        failFast = builder.failFast;
        failOnMissingRule = builder.failOnMissingRule;
        locale = builder.locale;
        clock = builder.clock;
        parameters = Collections.unmodifiableMap(
                new LinkedHashMap<String, String>(builder.parameters));
        attributes = Collections.unmodifiableMap(
                new LinkedHashMap<String, Object>(builder.attributes));
    }

    public static BusinessValidationOptions defaults() {
        return DEFAULTS;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isFailFast() {
        return failFast;
    }

    public boolean isFailOnMissingRule() {
        return failOnMissingRule;
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

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /** 通过链式 API 构建不可变执行策略。 */
    public static final class Builder {

        private boolean failFast;
        private boolean failOnMissingRule = true;
        private Locale locale = Locale.getDefault();
        private Clock clock = Clock.systemDefaultZone();
        private final Map<String, String> parameters =
                new LinkedHashMap<String, String>();
        private final Map<String, Object> attributes =
                new LinkedHashMap<String, Object>();

        private Builder() {
        }

        public Builder failFast(boolean value) {
            failFast = value;
            return this;
        }

        public Builder failOnMissingRule(boolean value) {
            failOnMissingRule = value;
            return this;
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

        public Builder attribute(String name, Object value) {
            String key = normalizeName(name);
            if (key != null) {
                attributes.put(key, value);
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

        public Builder attributes(Map<String, ?> values) {
            if (values != null) {
                for (Map.Entry<String, ?> entry : values.entrySet()) {
                    attribute(entry.getKey(), entry.getValue());
                }
            }
            return this;
        }

        public BusinessValidationOptions build() {
            return new BusinessValidationOptions(this);
        }

        private static String normalizeName(String value) {
            if (value == null || value.trim().isEmpty()) {
                return null;
            }
            return value.trim();
        }
    }
}
