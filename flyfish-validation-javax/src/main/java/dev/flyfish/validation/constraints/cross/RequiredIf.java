package dev.flyfish.validation.constraints.cross;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import dev.flyfish.validation.validator.cross.RequiredIfValidator;

/**
 * 条件字段命中指定值时目标字段必填。
 *
 * <p>该类级约束可重复声明，用于同一命令对象上的多组字段关系。</p>
 */
@Documented
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RequiredIf.List.class)
@Constraint(validatedBy = RequiredIfValidator.class)
public @interface RequiredIf {

    String message() default "{dev.flyfish.validation.constraints.cross.RequiredIf.message}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String conditionField();

    String[] conditionValues();

    String requiredField();

    boolean ignoreCase() default false;

    String reportOn() default "";

    /** 同一类型约束的容器，由 Java {@link Repeatable} 机制使用。 */
    @Documented
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        RequiredIf[] value();
    }
}
