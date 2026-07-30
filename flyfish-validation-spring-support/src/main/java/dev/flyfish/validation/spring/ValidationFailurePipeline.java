package dev.flyfish.validation.spring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dev.flyfish.validation.api.ValidationError;
import dev.flyfish.validation.api.ValidationReport;

/**
 * 错误定制、脱敏和最终失败行为的固定编排流水线。
 *
 * <p>定制器先加工错误码和字段路径，随后统一脱敏，最后才交给响应处理器，
 * 确保任何业务定制都无法绕过最终的敏感信息保护。</p>
 */
public final class ValidationFailurePipeline {

    private final ValidationRejectedValueSanitizer sanitizer;
    private final List<ValidationErrorCustomizer> customizers;
    private final ValidationFailureHandler handler;

    public ValidationFailurePipeline(
    ValidationRejectedValueSanitizer sanitizer,
    List<? extends ValidationErrorCustomizer> customizers,
    ValidationFailureHandler handler) {
        if (sanitizer == null) {
            throw new IllegalArgumentException("sanitizer 不能为空");
        }
        if (handler == null) {
            throw new IllegalArgumentException("handler 不能为空");
        }
        this.sanitizer = sanitizer;
        List<ValidationErrorCustomizer> copy =
        new ArrayList<ValidationErrorCustomizer>();
        if (customizers != null) {
            copy.addAll(customizers);
        }
        Collections.sort(copy,
        new Comparator<ValidationErrorCustomizer>() {
            @Override
            public int compare(ValidationErrorCustomizer left,
            ValidationErrorCustomizer right) {
                return Integer.compare(left.order(), right.order());
            }
        });
        this.customizers = Collections.unmodifiableList(copy);
        this.handler = handler;
    }

    public ValidationFailureAction handle(Throwable cause,
    List<ValidationError> errors,
    String path, String method) {
        ValidationReport.Builder report = ValidationReport.builder();
        if (errors != null) {
            for (ValidationError source : errors) {
                ValidationError current = source;
                for (ValidationErrorCustomizer customizer : customizers) {
                    if (current == null) {
                        break;
                    }
                    current = customizer.customize(current, cause);
                }
                if (current != null) {
                    report.error(sanitizer.sanitize(current));
                }
            }
        }
        return handler.handle(new ValidationFailure(cause,
        report.build(), path, method));
    }
}
