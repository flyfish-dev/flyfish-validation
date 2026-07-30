package dev.flyfish.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import dev.flyfish.validation.validator.ChinaIdCardValidator;

/** 中国居民身份证号码。空值默认有效，必填请组合标准约束。 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
    ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ChinaIdCardValidator.class)
public @interface ChinaIdCard {
    String message() default "{dev.flyfish.validation.constraints.ChinaIdCard.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowLegacy15() default false;
    int maximumAge() default 150;
}
