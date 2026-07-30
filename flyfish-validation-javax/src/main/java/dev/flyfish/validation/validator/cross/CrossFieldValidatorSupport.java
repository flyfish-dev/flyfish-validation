package dev.flyfish.validation.validator.cross;

import java.time.Clock;

import javax.validation.ClockProvider;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.support.BeanPropertyAccess;

/**
 * 跨字段验证器共享的属性读取、空值判断、时钟获取和字段级错误定位能力。
 *
 * <p>属性读取失败时不抛出反射异常，而是按约束失败处理，确保请求参数校验
 * 始终输出稳定的业务错误。</p>
 */
abstract class CrossFieldValidatorSupport {

    protected static BeanPropertyAccess.PropertyValue read(
            Object bean, String path) {
        return BeanPropertyAccess.read(bean, path);
    }

    protected static boolean absent(Object value, boolean blankAsNull) {
        return value == null
                || blankAsNull && value instanceof CharSequence
                && value.toString().trim().isEmpty();
    }

    protected static boolean reject(
            ConstraintValidatorContext context, String reportOn) {
        if (context == null) {
            return false;
        }
        String template = context.getDefaultConstraintMessageTemplate();
        context.disableDefaultConstraintViolation();
        if (reportOn == null || reportOn.trim().isEmpty()) {
            context.buildConstraintViolationWithTemplate(template)
                    .addConstraintViolation();
        } else {
            context.buildConstraintViolationWithTemplate(template)
                    .addPropertyNode(reportOn.trim())
                    .addConstraintViolation();
        }
        return false;
    }

    protected static Clock clock(ConstraintValidatorContext context) {
        if (context == null) {
            return Clock.systemDefaultZone();
        }
        try {
            ClockProvider provider = context.getClockProvider();
            return provider == null || provider.getClock() == null
                    ? Clock.systemDefaultZone() : provider.getClock();
        } catch (RuntimeException exception) {
            return Clock.systemDefaultZone();
        }
    }

    protected static String reportPath(
            String configuredPath, String fallbackPath) {
        return configuredPath == null || configuredPath.trim().isEmpty()
                ? fallbackPath : configuredPath.trim();
    }
}
