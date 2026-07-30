package dev.flyfish.validation.validator.cross;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.cross.IdCardBirthdayConsistent;
import dev.flyfish.validation.support.BeanPropertyAccess.PropertyValue;
import dev.flyfish.validation.support.ChineseIdCardInfo;
import dev.flyfish.validation.support.ChineseIdCardParser;

/** 验证身份证号码中的出生日期与独立生日字段一致。 */
public final class IdCardBirthdayConsistentValidator
        extends CrossFieldValidatorSupport
        implements ConstraintValidator<IdCardBirthdayConsistent, Object> {

    private String idCard;
    private String birthday;
    private String pattern;
    private boolean allowLegacy15;
    private String reportOn;

    @Override
    public void initialize(IdCardBirthdayConsistent annotation) {
        idCard = annotation.idCard();
        birthday = annotation.birthday();
        pattern = annotation.pattern();
        allowLegacy15 = annotation.allowLegacy15();
        reportOn = annotation.reportOn();
    }

    @Override
    public boolean isValid(
            Object bean, ConstraintValidatorContext context) {
        if (bean == null) {
            return true;
        }
        PropertyValue idCardValue = read(bean, idCard);
        PropertyValue birthdayValue = read(bean, birthday);
        String errorPath = reportPath(reportOn, birthday);
        if (!idCardValue.isPresent() || !birthdayValue.isPresent()) {
            return reject(context, errorPath);
        }
        if (idCardValue.getValue() == null
                || birthdayValue.getValue() == null) {
            return true;
        }
        try {
            Optional<ChineseIdCardInfo> card = ChineseIdCardParser.parse(
                    idCardValue.getValue().toString(),
                    allowLegacy15, clock(context));
            LocalDate date = toLocalDate(birthdayValue.getValue());
            return card.isPresent()
                    && card.get().getBirthday().equals(date)
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
