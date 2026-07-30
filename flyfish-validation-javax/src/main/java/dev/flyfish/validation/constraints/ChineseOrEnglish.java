package dev.flyfish.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import dev.flyfish.validation.validator.ChineseOrEnglishValidator;

/** 中英文字符策略。空值默认有效，必填请组合标准约束。 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
    ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ChineseOrEnglishValidator.class)
public @interface ChineseOrEnglish {
    String message() default "{dev.flyfish.validation.constraints.ChineseOrEnglish.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean allowWhitespace() default true;
    boolean allowDigits() default false;
    boolean allowPunctuation() default true;
}
