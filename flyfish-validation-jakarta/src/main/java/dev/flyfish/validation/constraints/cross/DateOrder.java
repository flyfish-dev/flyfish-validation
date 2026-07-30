package dev.flyfish.validation.constraints.cross;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import dev.flyfish.validation.validator.cross.DateOrderValidator;

/**
 * 开始日期不得晚于结束日期。
 *
 * <p>该类级约束可重复声明，用于同一命令对象上的多组字段关系。</p>
 */
@Documented
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(DateOrder.List.class)
@Constraint(validatedBy = DateOrderValidator.class)
public @interface DateOrder {

    String message() default "{dev.flyfish.validation.constraints.cross.DateOrder.message}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String start();

    String end();

    String pattern() default "yyyy-MM-dd";

    boolean allowEqual() default true;

    String reportOn() default "";

    /** 同一类型约束的容器，由 Java {@link Repeatable} 机制使用。 */
    @Documented
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        DateOrder[] value();
    }
}
