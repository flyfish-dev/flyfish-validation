package dev.flyfish.validation.validator.cross;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.cross.BirthdayAgeConsistent;
import dev.flyfish.validation.support.BeanPropertyAccess.PropertyValue;

/**
 * 验证生日与声明年龄一致。
 *
 * <p>当前日期来自 Bean Validation 的 {@code ClockProvider}，因此测试和多时区
 * 部署不会依赖机器当前时间。</p>
 */
public final class BirthdayAgeConsistentValidator
        extends CrossFieldValidatorSupport
        implements ConstraintValidator<BirthdayAgeConsistent, Object> {

    private String birthday;
    private String age;
    private String pattern;
    private String zone;
    private int tolerance;
    private String reportOn;

    @Override
    public void initialize(BirthdayAgeConsistent annotation) {
        birthday = annotation.birthday();
        age = annotation.age();
        pattern = annotation.pattern();
        zone = annotation.zone();
        tolerance = annotation.tolerance();
        reportOn = annotation.reportOn();
    }

    @Override
    public boolean isValid(
            Object bean, ConstraintValidatorContext context) {
        if (bean == null) {
            return true;
        }
        PropertyValue birthdayValue = read(bean, birthday);
        PropertyValue ageValue = read(bean, age);
        String errorPath = reportPath(reportOn, age);
        if (!birthdayValue.isPresent() || !ageValue.isPresent()) {
            return reject(context, errorPath);
        }
        if (birthdayValue.getValue() == null || ageValue.getValue() == null) {
            return true;
        }
        try {
            LocalDate date = toLocalDate(birthdayValue.getValue());
            ZoneId zoneId = zone == null || zone.trim().isEmpty()
                    ? clock(context).getZone() : ZoneId.of(zone);
            LocalDate today = LocalDate.now(clock(context).withZone(zoneId));
            int actualAge = Period.between(date, today).getYears();
            int declaredAge = Integer.parseInt(
                    ageValue.getValue().toString());
            return Math.abs(actualAge - declaredAge) <= tolerance
                    || reject(context, errorPath);
        } catch (RuntimeException exception) {
            return reject(context, errorPath);
        }
    }

    private LocalDate toLocalDate(Object value) {
        if (value instanceof LocalDate) {
            return (LocalDate) value;
        }
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern(pattern.replace("yyyy", "uuuu"))
                .withResolverStyle(ResolverStyle.STRICT);
        return LocalDate.parse(value.toString(), formatter);
    }
}
