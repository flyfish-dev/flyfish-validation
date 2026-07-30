package dev.flyfish.validation.constraints.cross;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import dev.flyfish.validation.validator.cross.AtLeastOneNotBlankValidator;

/**
 * 至少一个字段必须为非空文本。
 *
 * <p>该类级约束可重复声明，用于同一命令对象上的多组字段关系。</p>
 */
@Documented
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AtLeastOneNotBlank.List.class)
@Constraint(validatedBy = AtLeastOneNotBlankValidator.class)
public @interface AtLeastOneNotBlank {

    String message() default "{dev.flyfish.validation.constraints.cross.AtLeastOneNotBlank.message}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String[] fields();

    String reportOn() default "";

    /** 同一类型约束的容器，由 Java {@link Repeatable} 机制使用。 */
    @Documented
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        AtLeastOneNotBlank[] value();
    }
}
