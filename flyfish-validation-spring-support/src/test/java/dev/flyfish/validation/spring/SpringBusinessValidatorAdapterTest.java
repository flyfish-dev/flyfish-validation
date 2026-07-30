package dev.flyfish.validation.spring;

import org.junit.jupiter.api.Test;
import org.springframework.validation.Errors;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import dev.flyfish.validation.api.ValidationReport;
import dev.flyfish.validation.business.BusinessValidationContext;

/** Spring Validator 自动适配和敏感值脱敏回归测试。 */
class SpringBusinessValidatorAdapterTest {

    @Test
    void shouldAdaptErrorsAndRemoveRejectedValue() {
        SpringBusinessValidator validator = new SpringBusinessValidator() {
            @Override public String key() { return "username-unique"; }
            @Override public boolean supports(Class<?> clazz) {
                return Command.class.isAssignableFrom(clazz);
            }
            @Override public void validate(Object target, Errors errors) {
                Command command = (Command) target;
                if ("admin".equals(command.getUsername())) {
                    errors.rejectValue("username", "USERNAME_TAKEN",
                    "用户名已存在");
                }
            }
        };

        ValidationReport report = new SpringBusinessValidatorAdapter(validator)
            .validate(new Command("admin"),
        BusinessValidationContext.builder("username-unique")
            .build());
        assertFalse(report.isValid());
        assertEquals("username", report.getErrors().get(0).getPropertyPath());

        DefaultValidationRejectedValueSanitizer sanitizer =
        new DefaultValidationRejectedValueSanitizer(false, true);
        assertEquals(null,
        sanitizer.sanitize(report.getErrors().get(0))
            .getRejectedValue());
    }

    private static final class Command {
        private final String username;
        private Command(String username) { this.username = username; }
        public String getUsername() { return username; }
    }
}
