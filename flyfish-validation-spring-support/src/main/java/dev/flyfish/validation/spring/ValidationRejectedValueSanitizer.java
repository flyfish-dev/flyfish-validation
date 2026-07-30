package dev.flyfish.validation.spring;

import dev.flyfish.validation.api.ValidationError;

/** 控制错误响应是否保留被拒绝原值。 */
public interface ValidationRejectedValueSanitizer {
    ValidationError sanitize(ValidationError error);
}
