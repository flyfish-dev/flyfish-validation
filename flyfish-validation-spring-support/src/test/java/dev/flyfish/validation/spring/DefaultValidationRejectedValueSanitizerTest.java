package dev.flyfish.validation.spring;

import dev.flyfish.validation.api.ValidationError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** 敏感字段即使启用 rejected value 诊断开关也必须保持脱敏。 */
class DefaultValidationRejectedValueSanitizerTest {

    private final DefaultValidationRejectedValueSanitizer sanitizer =
        new DefaultValidationRejectedValueSanitizer(true, false);

    @Test
    void exposesRejectedValueForOrdinaryFieldsWhenExplicitlyEnabled() {
        ValidationError error = error("displayName", "张三");

        assertEquals("张三", sanitizer.sanitize(error).getRejectedValue());
    }

    @Test
    void neverExposesSensitiveRejectedValues() {
        // 离线兼容检查使用最小 JUnit API 桩；用标准等值断言保持测试语义且不扩大桩接口。
        assertEquals(null, sanitizer.sanitize(error("password", "Plaintext-Secret")).getRejectedValue());
        assertEquals(null, sanitizer.sanitize(error("credentials.clientSecret", "secret")).getRejectedValue());
        assertEquals(null, sanitizer.sanitize(error("oauth.access_token", "token")).getRejectedValue());
    }

    private static ValidationError error(String propertyPath, Object rejectedValue) {
        return ValidationError.builder("INVALID", "校验失败")
            .propertyPath(propertyPath)
            .rejectedValue(rejectedValue)
            .build();
    }
}
