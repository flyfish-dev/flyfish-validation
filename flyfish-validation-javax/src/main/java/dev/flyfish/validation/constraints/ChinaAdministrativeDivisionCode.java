package dev.flyfish.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import dev.flyfish.validation.region.AdministrativeDivisionLevel;
import dev.flyfish.validation.validator.ChinaAdministrativeDivisionCodeValidator;

/** 中国行政区划代码。空值默认有效，必填请组合标准约束。 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
    ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ChinaAdministrativeDivisionCodeValidator.class)
public @interface ChinaAdministrativeDivisionCode {
    String message() default "{dev.flyfish.validation.constraints.ChinaAdministrativeDivisionCode.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    AdministrativeDivisionLevel level() default AdministrativeDivisionLevel.AUTO;
}
