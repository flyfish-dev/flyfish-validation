package dev.flyfish.validation.constraints.cross;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import dev.flyfish.validation.validator.cross.ExactlyOneNotNullValidator;

/**
 * 恰好一个字段必须非 null。
 *
 * <p>该类级约束可重复声明，用于同一命令对象上的多组字段关系。</p>
 */
@Documented
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ExactlyOneNotNull.List.class)
@Constraint(validatedBy = ExactlyOneNotNullValidator.class)
public @interface ExactlyOneNotNull {

    String message() default "{dev.flyfish.validation.constraints.cross.ExactlyOneNotNull.message}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String[] fields();

    String reportOn() default "";

    /** 同一类型约束的容器，由 Java {@link Repeatable} 机制使用。 */
    @Documented
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        ExactlyOneNotNull[] value();
    }
}
