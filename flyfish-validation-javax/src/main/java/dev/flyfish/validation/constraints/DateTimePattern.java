package dev.flyfish.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import dev.flyfish.validation.validator.DateTimePatternValidator;

/** 严格日期时间格式。空值默认有效，必填请组合标准约束。 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
    ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateTimePatternValidator.class)
public @interface DateTimePattern {
    String message() default "{dev.flyfish.validation.constraints.DateTimePattern.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String pattern() default "yyyy-MM-dd HH:mm:ss";
}
