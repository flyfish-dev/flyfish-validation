package dev.flyfish.validation.integration;

import java.time.LocalDate;
import java.time.Period;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.flyfish.validation.constraints.Age;
import dev.flyfish.validation.constraints.Birthday;
import dev.flyfish.validation.constraints.ChinaIdCard;
import dev.flyfish.validation.constraints.ChineseMobile;
import dev.flyfish.validation.constraints.StrictEmail;
import dev.flyfish.validation.constraints.StrongPassword;
import dev.flyfish.validation.constraints.cross.BirthdayAgeConsistent;
import dev.flyfish.validation.constraints.cross.FieldsMatch;

/** 在真实 Hibernate Validator Provider 上验证注解注册、消息和字段路径。 */
class ConstraintIntegrationTest {

    private final Validator validator = Validation
        .buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldAcceptValidRegistration() {
        LocalDate birthday = LocalDate.of(1990, 6, 15);
        Registration value = new Registration(
        "13800138000", "11010519491231002X",
        "engineer@example.com", "Flyfish@2026",
        "Flyfish@2026", birthday.toString(),
        Period.between(birthday, LocalDate.now()).getYears());

        Set<ConstraintViolation<Registration>> violations =
        validator.validate(value);
        assertTrue(violations.isEmpty(), violations.toString());
    }

    @Test
    void shouldReportFieldAndCrossFieldViolations() {
        Registration value = new Registration(
        "12800138000", "110105194912310021",
        "invalid", "weak", "different",
        "1990-06-15", 18);

        Set<ConstraintViolation<Registration>> violations =
        validator.validate(value);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(item ->
        "mobile".equals(item.getPropertyPath().toString())));
        assertTrue(violations.stream().anyMatch(item ->
        "confirmation".equals(item.getPropertyPath().toString())));
    }

    @FieldsMatch(first = "password", second = "confirmation",
    reportOn = "confirmation")
    @BirthdayAgeConsistent(birthday = "birthday", age = "age",
    reportOn = "age")
    static final class Registration {
        @ChineseMobile
        private final String mobile;
        @ChinaIdCard
        private final String idCard;
        @StrictEmail
        private final String email;
        @StrongPassword
        private final String password;
        private final String confirmation;
        @Birthday
        private final String birthday;
        @Age
        private final Integer age;

        Registration(String mobile, String idCard, String email,
        String password, String confirmation,
        String birthday, Integer age) {
            this.mobile = mobile;
            this.idCard = idCard;
            this.email = email;
            this.password = password;
            this.confirmation = confirmation;
            this.birthday = birthday;
            this.age = age;
        }

        public String getMobile() { return mobile; }
        public String getIdCard() { return idCard; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public String getConfirmation() { return confirmation; }
        public String getBirthday() { return birthday; }
        public Integer getAge() { return age; }
    }
}
