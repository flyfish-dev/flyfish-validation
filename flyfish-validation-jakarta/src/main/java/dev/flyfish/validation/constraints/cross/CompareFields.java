package dev.flyfish.validation.constraints.cross;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import dev.flyfish.validation.model.ComparisonOperator;
import dev.flyfish.validation.validator.cross.CompareFieldsValidator;

/**
 * 按操作符比较两个字段。
 *
 * <p>该类级约束可重复声明，用于同一命令对象上的多组字段关系。</p>
 */
@Documented
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CompareFields.List.class)
@Constraint(validatedBy = CompareFieldsValidator.class)
public @interface CompareFields {

    String message() default "{dev.flyfish.validation.constraints.cross.CompareFields.message}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String left();

    String right();

    ComparisonOperator operator() default ComparisonOperator.EQUAL;

    String pattern() default "";

    String reportOn() default "";

    /** 同一类型约束的容器，由 Java {@link Repeatable} 机制使用。 */
    @Documented
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        CompareFields[] value();
    }
}
