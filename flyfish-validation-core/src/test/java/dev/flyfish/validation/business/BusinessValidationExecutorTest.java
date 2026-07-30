package dev.flyfish.validation.business;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.flyfish.validation.api.ValidationError;
import dev.flyfish.validation.api.ValidationReport;

/** 数据库或远程关联规则编排能力的回归测试。 */
class BusinessValidationExecutorTest {

    @Test
    void shouldCollectRulesInStableOrder() {
        BusinessValidator<String> reserved = rule("reserved", 20, "admin");
        BusinessValidator<String> duplicate = rule("duplicate", 10, "admin");
        DefaultBusinessValidatorRegistry registry =
        new DefaultBusinessValidatorRegistry(
        Arrays.asList(reserved, duplicate),
        Collections.<AsyncBusinessValidator<?>>emptyList());

        ValidationReport report = new BusinessValidationExecutor(registry)
            .validate("admin", "duplicate", "reserved");

        assertFalse(report.isValid());
        assertEquals(2, report.getErrors().size());
        assertEquals("duplicate", report.getErrors().get(0).getValidator());
        assertEquals("reserved", report.getErrors().get(1).getValidator());
    }

    @Test
    void shouldSupportFailFastAndAsyncRule() throws Exception {
        BusinessValidator<String> first = rule("first", 0, "blocked");
        AsyncBusinessValidator<String> remote =
        new AsyncBusinessValidator<String>() {
            @Override public String key() { return "remote"; }
            @Override public Class<String> targetType() {
                return String.class;
            }
            @Override
            public java.util.concurrent.CompletionStage<ValidationReport>
            validateAsync(String value,
            BusinessValidationContext context) {
                return CompletableFuture.completedFuture(
                ValidationReport.valid());
            }
        };
        DefaultBusinessValidatorRegistry registry =
        new DefaultBusinessValidatorRegistry(
        Collections.<BusinessValidator<?>>singletonList(first),
        Collections.<AsyncBusinessValidator<?>>singletonList(remote));
        BusinessValidationExecutor executor =
        new BusinessValidationExecutor(registry);

        ValidationReport fast = executor.validate("blocked",
        Arrays.asList("first", "missing"),
        BusinessValidationOptions.builder().failFast(true).build());
        assertEquals(1, fast.getErrors().size());
        assertTrue(executor.validateAsync("normal", "remote")
            .toCompletableFuture().get().isValid());
    }

    private static BusinessValidator<String> rule(
    final String key, final int order, final String denied) {
        return new BusinessValidator<String>() {
            @Override public String key() { return key; }
            @Override public Class<String> targetType() { return String.class; }
            @Override public int order() { return order; }
            @Override
            public ValidationReport validate(String value,
            BusinessValidationContext context) {
                if (!denied.equals(value)) {
                    return ValidationReport.valid();
                }
                return ValidationReport.invalid(ValidationError.builder(
                "VALUE_DENIED", "值已被业务规则拒绝")
                    .validator(key).build());
            }
        };
    }
}
