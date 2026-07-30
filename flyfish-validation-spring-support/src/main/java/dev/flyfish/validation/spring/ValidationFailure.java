package dev.flyfish.validation.spring;

import dev.flyfish.validation.api.ValidationReport;

/** 交给 HTTP 失败处理器的不可变上下文。 */
public final class ValidationFailure {
    private final Throwable cause;
    private final ValidationReport report;
    private final String path;
    private final String method;
    public ValidationFailure(Throwable cause, ValidationReport report,
    String path, String method) {
        this.cause = cause;
        this.report = report == null ? ValidationReport.valid() : report;
        this.path = path == null ? "" : path;
        this.method = method == null ? "" : method;
    }
    public Throwable getCause() { return cause; }
    public ValidationReport getReport() { return report; }
    public String getPath() { return path; }
    public String getMethod() { return method; }
}
