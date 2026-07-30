package dev.flyfish.validation.constraints.cross;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import dev.flyfish.validation.validator.cross.NumberOrderValidator;

/**
 * 较小数值不得大于较大数值。
 *
 * <p>该类级约束可重复声明，用于同一命令对象上的多组字段关系。</p>
 */
@Documented
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(NumberOrder.List.class)
@Constraint(validatedBy = NumberOrderValidator.class)
public @interface NumberOrder {

    String message() default "{dev.flyfish.validation.constraints.cross.NumberOrder.message}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String smaller();

    String larger();

    boolean allowEqual() default true;

    String reportOn() default "";

    /** 同一类型约束的容器，由 Java {@link Repeatable} 机制使用。 */
    @Documented
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        NumberOrder[] value();
    }
}
