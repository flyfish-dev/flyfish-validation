package dev.flyfish.validation.validator;

import java.time.Clock;

import jakarta.validation.ClockProvider;
import jakarta.validation.ConstraintValidatorContext;

/** 内置验证器共享的空值、时钟与字段路径辅助。 */
abstract class ConstraintValidatorSupport {
    protected static boolean nullable(Object value) { return value == null; }
    protected static Clock clock(ConstraintValidatorContext context) {
        if (context == null) { return Clock.systemDefaultZone(); }
        try {
            ClockProvider provider = context.getClockProvider();
            return provider == null || provider.getClock() == null
                ? Clock.systemDefaultZone() : provider.getClock();
        } catch (RuntimeException exception) {
            return Clock.systemDefaultZone();
        }
    }
}
