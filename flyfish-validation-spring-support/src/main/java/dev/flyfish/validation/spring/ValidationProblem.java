package dev.flyfish.validation.spring;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.flyfish.validation.api.ValidationError;

/** 默认验证错误响应，字段稳定且不携带异常堆栈或原始请求对象。 */
public final class ValidationProblem {

    private final Instant timestamp;
    private final int status;
    private final String code;
    private final String message;
    private final String path;
    private final String method;
    private final List<ValidationError> errors;

    public ValidationProblem(int status, String code, String message,
    String path, String method,
    List<ValidationError> errors) {
        timestamp = Instant.now();
        this.status = status;
        this.code = code == null ? "VALIDATION_FAILED" : code;
        this.message = message == null ? "请求参数验证失败" : message;
        this.path = path == null ? "" : path;
        this.method = method == null ? "" : method;
        this.errors = Collections.unmodifiableList(errors == null
            ? new ArrayList<ValidationError>()
            : new ArrayList<ValidationError>(errors));
    }

    public Instant getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getCode() { return code; }
    public String getMessage() { return message; }
    public String getPath() { return path; }
    public String getMethod() { return method; }
    public List<ValidationError> getErrors() { return errors; }
}
