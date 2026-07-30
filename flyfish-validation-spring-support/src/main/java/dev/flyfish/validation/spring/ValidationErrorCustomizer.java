package dev.flyfish.validation.spring;

import dev.flyfish.validation.api.ValidationError;

/** 统一响应输出前对错误码、路径或属性做业务定制。 */
public interface ValidationErrorCustomizer {
    default int order() { return 0; }
    ValidationError customize(ValidationError error, Throwable cause);
}
