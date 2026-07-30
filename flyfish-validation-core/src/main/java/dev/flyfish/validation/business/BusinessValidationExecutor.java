package dev.flyfish.validation.business;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import dev.flyfish.validation.api.ValidationError;
import dev.flyfish.validation.api.ValidationException;
import dev.flyfish.validation.api.ValidationInvocation;
import dev.flyfish.validation.api.ValidationLifecycleListener;
import dev.flyfish.validation.api.ValidationReport;

/**
 * 按声明顺序执行同步或异步业务规则，并统一生命周期、错误模型和
 * fail-fast 行为。
 *
 * <p>执行器本身不依赖 Spring，也不持有请求状态。规则注册表创建完成后不可变，
 * 因而可以作为单例安全复用。</p>
 */
public final class BusinessValidationExecutor {

    private static final ValidationLifecycleListener NO_OP_LISTENER =
            new ValidationLifecycleListener() { };

    private final BusinessValidatorRegistry registry;
    private final ValidationLifecycleListener listener;

    public BusinessValidationExecutor(BusinessValidatorRegistry registry) {
        this(registry, NO_OP_LISTENER);
    }

    public BusinessValidationExecutor(
            BusinessValidatorRegistry registry,
            ValidationLifecycleListener listener) {
        if (registry == null) {
            throw new IllegalArgumentException("registry 不能为空");
        }
        this.registry = registry;
        this.listener = listener == null ? NO_OP_LISTENER : listener;
    }

    public ValidationReport validate(Object value, String... rules) {
        Iterable<String> selectedRules = rules == null
                ? Collections.<String>emptyList() : Arrays.asList(rules);
        return validate(
                value, selectedRules, BusinessValidationOptions.defaults());
    }

    public ValidationReport validate(
            Object value, Iterable<String> rules,
            BusinessValidationOptions options) {
        BusinessValidationOptions effective = options == null
                ? BusinessValidationOptions.defaults() : options;
        ValidationInvocation invocation = new ValidationInvocation(
                value, "business", effective.getAttributes());
        long start = System.nanoTime();
        listener.beforeValidation(invocation);
        try {
            ValidationReport.Builder merged = ValidationReport.builder();
            if (rules != null) {
                for (String rawRule : rules) {
                    String rule = normalize(rawRule);
                    ValidationReport current =
                            validateOne(value, rule, effective);
                    merged.report(current);
                    if (!current.isValid() && effective.isFailFast()) {
                        break;
                    }
                }
            }
            ValidationReport report = merged.build();
            notifyOutcome(
                    invocation, report, System.nanoTime() - start);
            return report;
        } catch (RuntimeException exception) {
            listener.afterException(
                    invocation, exception, System.nanoTime() - start);
            throw exception;
        }
    }

    public ValidationReport validateOrThrow(
            Object value, String... rules) {
        ValidationReport report = validate(value, rules);
        if (!report.isValid()) {
            throw new ValidationException(report);
        }
        return report;
    }

    public CompletionStage<ValidationReport> validateAsync(
            Object value, String... rules) {
        Iterable<String> selectedRules = rules == null
                ? Collections.<String>emptyList() : Arrays.asList(rules);
        return validateAsync(
                value, selectedRules, BusinessValidationOptions.defaults());
    }

    public CompletionStage<ValidationReport> validateAsync(
            final Object value, Iterable<String> rules,
            final BusinessValidationOptions options) {
        final BusinessValidationOptions effective = options == null
                ? BusinessValidationOptions.defaults() : options;
        final ValidationInvocation invocation = new ValidationInvocation(
                value, "business-async", effective.getAttributes());
        final long start = System.nanoTime();
        listener.beforeValidation(invocation);

        CompletionStage<ValidationReport> stage =
                CompletableFuture.completedFuture(ValidationReport.valid());
        if (rules != null) {
            for (String rawRule : rules) {
                final String rule = normalize(rawRule);
                stage = stage.thenCompose(previous -> {
                    if (!previous.isValid() && effective.isFailFast()) {
                        return CompletableFuture.completedFuture(previous);
                    }
                    return validateOneAsync(value, rule, effective)
                            .thenApply(previous::merge);
                });
            }
        }
        return stage.whenComplete((report, error) -> {
            long elapsed = System.nanoTime() - start;
            if (error == null) {
                notifyOutcome(invocation, report, elapsed);
            } else {
                RuntimeException exception = error instanceof RuntimeException
                        ? (RuntimeException) error
                        : new IllegalStateException(error);
                listener.afterException(invocation, exception, elapsed);
            }
        });
    }

    private ValidationReport validateOne(
            Object value, String rule,
            BusinessValidationOptions options) {
        ValidationReport configuration = configurationError(rule, options);
        if (configuration != null) {
            return configuration;
        }
        BusinessValidator<?> validator = registry.synchronous(rule);
        if (validator == null) {
            return ValidationReport.invalid(ValidationError.builder(
                            "BUSINESS_VALIDATOR_ASYNC_ONLY",
                            "规则仅支持异步执行")
                    .propertyPath("")
                    .validator(rule)
                    .build());
        }
        return invoke(validator, value, context(rule, options));
    }

    private CompletionStage<ValidationReport> validateOneAsync(
            Object value, String rule,
            BusinessValidationOptions options) {
        ValidationReport configuration = configurationError(rule, options);
        if (configuration != null) {
            return CompletableFuture.completedFuture(configuration);
        }
        AsyncBusinessValidator<?> asynchronous =
                registry.asynchronous(rule);
        if (asynchronous != null) {
            return invokeAsync(
                    asynchronous, value, context(rule, options));
        }
        return CompletableFuture.completedFuture(invoke(
                registry.synchronous(rule), value, context(rule, options)));
    }

    private ValidationReport configurationError(
            String rule, BusinessValidationOptions options) {
        if (rule.isEmpty()) {
            return ValidationReport.invalid(ValidationError.builder(
                    "BUSINESS_VALIDATOR_NAME_INVALID",
                    "业务验证规则名不能为空").build());
        }
        if (!registry.contains(rule)) {
            if (!options.isFailOnMissingRule()) {
                return ValidationReport.valid();
            }
            return ValidationReport.invalid(ValidationError.builder(
                            "BUSINESS_VALIDATOR_NOT_FOUND",
                            "未注册业务验证规则: " + rule)
                    .validator(rule)
                    .attribute("rule", rule)
                    .build());
        }
        return null;
    }

    private static BusinessValidationContext context(
            String rule, BusinessValidationOptions options) {
        return BusinessValidationContext.builder(rule)
                .locale(options.getLocale())
                .clock(options.getClock())
                .parameters(options.getParameters())
                .attributes(options.getAttributes())
                .build();
    }

    @SuppressWarnings("unchecked")
    private static ValidationReport invoke(
            BusinessValidator<?> candidate, Object value,
            BusinessValidationContext context) {
        if (candidate == null) {
            throw new MissingBusinessValidatorException(context.getRule());
        }
        if (value != null && !candidate.targetType().isInstance(value)) {
            return typeMismatch(
                    context.getRule(), candidate.targetType(),
                    value.getClass());
        }
        BusinessValidator<Object> typed =
                (BusinessValidator<Object>) candidate;
        ValidationReport result = typed.validate(value, context);
        return result == null ? ValidationReport.valid() : result;
    }

    @SuppressWarnings("unchecked")
    private static CompletionStage<ValidationReport> invokeAsync(
            AsyncBusinessValidator<?> candidate, Object value,
            BusinessValidationContext context) {
        if (value != null && !candidate.targetType().isInstance(value)) {
            return CompletableFuture.completedFuture(typeMismatch(
                    context.getRule(), candidate.targetType(),
                    value.getClass()));
        }
        AsyncBusinessValidator<Object> typed =
                (AsyncBusinessValidator<Object>) candidate;
        CompletionStage<ValidationReport> stage =
                typed.validateAsync(value, context);
        if (stage == null) {
            CompletableFuture<ValidationReport> failed =
                    new CompletableFuture<ValidationReport>();
            failed.completeExceptionally(new IllegalStateException(
                    "异步业务验证器不能返回 null: " + context.getRule()));
            return failed;
        }
        return stage.thenApply(report -> report == null
                ? ValidationReport.valid() : report);
    }

    private static ValidationReport typeMismatch(
            String rule, Class<?> expectedType, Class<?> actualType) {
        return ValidationReport.invalid(ValidationError.builder(
                        "BUSINESS_VALIDATOR_TYPE_MISMATCH",
                        "业务规则不支持当前数据类型")
                .validator(rule)
                .attribute("expectedType", expectedType.getName())
                .attribute("actualType", actualType.getName())
                .build());
    }

    private void notifyOutcome(
            ValidationInvocation invocation,
            ValidationReport report, long elapsed) {
        ValidationReport effective = report == null
                ? ValidationReport.valid() : report;
        if (effective.isValid()) {
            listener.afterSuccess(invocation, effective, elapsed);
        } else {
            listener.afterFailure(invocation, effective, elapsed);
        }
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
