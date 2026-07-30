package dev.flyfish.validation.spring;

import dev.flyfish.validation.api.ValidationError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
        assertNull(sanitizer.sanitize(error("password", "Plaintext-Secret")).getRejectedValue());
        assertNull(sanitizer.sanitize(error("credentials.clientSecret", "secret")).getRejectedValue());
        assertNull(sanitizer.sanitize(error("oauth.access_token", "token")).getRejectedValue());
    }

    private static ValidationError error(String propertyPath, Object rejectedValue) {
        return ValidationError.builder("INVALID", "校验失败")
            .propertyPath(propertyPath)
            .rejectedValue(rejectedValue)
            .build();
    }
}
