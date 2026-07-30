package dev.flyfish.validation;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import dev.flyfish.validation.api.ValidationException;
import dev.flyfish.validation.api.ValidationInvocation;
import dev.flyfish.validation.api.ValidationLifecycleListener;
import dev.flyfish.validation.api.ValidationReport;
import dev.flyfish.validation.business.BusinessValidationExecutor;
import dev.flyfish.validation.business.BusinessValidationOptions;
import dev.flyfish.validation.business.BusinessValidatorRegistry;
import dev.flyfish.validation.business.DefaultBusinessValidatorRegistry;

/**
 * 同时封装 Bean Validation 与数据库关联业务规则的程序式验证门面。
 *
 * <p>Starter 会自动创建该 Bean；非 Spring 项目可使用
 * {@link #createDefault()}。实例无可变请求状态，可作为单例复用。</p>
 */
public final class FlyfishValidator {
    private final Validator validator;
    private final BusinessValidationExecutor business;
    private final ValidationLifecycleListener listener;

    public FlyfishValidator(Validator validator,
    BusinessValidatorRegistry registry,
    ValidationLifecycleListener listener) {
        if (validator == null) { throw new IllegalArgumentException("validator 不能为空"); }
        this.validator = validator;
        this.listener = listener == null ? new ValidationLifecycleListener() { } : listener;
        this.business = new BusinessValidationExecutor(
        registry == null ? DefaultBusinessValidatorRegistry.empty() : registry,
        this.listener);
    }

    public static FlyfishValidator createDefault() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return new FlyfishValidator(factory.getValidator(),
        DefaultBusinessValidatorRegistry.empty(), null);
    }

    /** 验证对象上的标准与 Flyfish Bean Validation 注解。 */
    public <T> ValidationReport validate(T target, Class<?>... groups) {
        return execute(target, "bean", () -> map(validator.validate(target, groups)));
    }

    /** 验证指定属性当前值。 */
    public <T> ValidationReport validateProperty(
    T target, String property, Class<?>... groups) {
        return execute(target, "bean-property",
        () -> map(validator.validateProperty(target, property, groups)));
    }

    /** 在没有对象实例时验证某一 Bean 类型的候选属性值。 */
    public <T> ValidationReport validateValue(Class<T> type,
    String property, Object value, Class<?>... groups) {
        return execute(value, "bean-value",
        () -> map(validator.validateValue(type, property, value, groups)));
    }

    public ValidationReport validateBusiness(Object target, String... rules) {
        return business.validate(target, rules);
    }

    public ValidationReport validateBusiness(Object target,
    Iterable<String> rules, BusinessValidationOptions options) {
        return business.validate(target, rules, options);
    }

    public CompletionStage<ValidationReport> validateBusinessAsync(
    Object target, String... rules) {
        return business.validateAsync(target, rules);
    }

    /** 先做无 I/O 的 Bean 校验，通过后再执行业务规则。 */
    public ValidationReport validateAll(Object target, String[] rules,
    Class<?>... groups) {
        ValidationReport bean = validate(target, groups);
        return bean.isValid() ? bean.merge(validateBusiness(target, rules)) : bean;
    }

    public <T> T validateOrThrow(T target, Class<?>... groups) {
        ValidationReport report = validate(target, groups);
        if (!report.isValid()) { throw new ValidationException(report); }
        return target;
    }

    public <T> T validateBusinessOrThrow(T target, String... rules) {
        ValidationReport report = validateBusiness(target, rules);
        if (!report.isValid()) { throw new ValidationException(report); }
        return target;
    }

    public Validator unwrap() { return validator; }

    private ValidationReport execute(Object target, String operation,
    ValidationOperation operationCall) {
        ValidationInvocation invocation = new ValidationInvocation(
        target, operation, Collections.<String,Object>emptyMap());
        long start = System.nanoTime();
        listener.beforeValidation(invocation);
        try {
            ValidationReport report = operationCall.run();
            long elapsed = System.nanoTime() - start;
            if (report.isValid()) { listener.afterSuccess(invocation, report, elapsed); }
            else { listener.afterFailure(invocation, report, elapsed); }
            return report;
        } catch (RuntimeException exception) {
            listener.afterException(invocation, exception,
            System.nanoTime() - start);
            throw exception;
        }
    }

    private static ValidationReport map(Set<? extends ConstraintViolation<?>> violations) {
        ValidationReport.Builder report = ValidationReport.builder();
        if (violations != null) {
            for (ConstraintViolation<?> violation : violations) {
                report.error(BeanValidationErrorMapper.map(violation));
            }
        }
        return report.build();
    }

    private interface ValidationOperation { ValidationReport run(); }
}
