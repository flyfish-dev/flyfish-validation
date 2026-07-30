package dev.flyfish.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import dev.flyfish.validation.validator.ChineseCharactersValidator;

/** 中文字符策略。空值默认有效，必填请组合标准约束。 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
    ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ChineseCharactersValidator.class)
public @interface ChineseCharacters {
    String message() default "{dev.flyfish.validation.constraints.ChineseCharacters.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowWhitespace() default true;
    boolean allowDigits() default false;
    boolean allowPunctuation() default true;
}
