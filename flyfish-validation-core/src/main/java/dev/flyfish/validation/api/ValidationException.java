package dev.flyfish.validation.api;

/** 程序式验证失败时抛出的统一异常。 */
@SuppressWarnings("serial")
public class ValidationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final ValidationReport report;

    public ValidationException(ValidationReport report) {
        this("业务数据验证失败", report);
    }

    public ValidationException(String message, ValidationReport report) {
        super(message == null ? "业务数据验证失败" : message);
        this.report = report == null ? ValidationReport.valid() : report;
    }

    public ValidationReport getReport() { return report; }
}
