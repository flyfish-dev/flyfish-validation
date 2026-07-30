package dev.flyfish.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import dev.flyfish.validation.validator.MoneyValidator;

/** 金额范围、精度和小数位。空值默认有效，必填请组合标准约束。 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
    ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MoneyValidator.class)
public @interface Money {
    String message() default "{dev.flyfish.validation.constraints.Money.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String min() default "";
    String max() default "";
    int fraction() default 2;
    int precision() default 19;
    boolean allowNegative() default false;
}
