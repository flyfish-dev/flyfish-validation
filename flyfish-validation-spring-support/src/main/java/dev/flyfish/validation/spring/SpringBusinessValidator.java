package dev.flyfish.validation.spring;

import org.springframework.validation.Validator;

/**
 * 在 Spring {@link Validator} 之上增加稳定规则 key 和执行顺序。
 *
 * <p>实现类声明为 Spring Bean 后会被三代 Starter 自动转换为 Flyfish
 * 业务规则，现有 {@code supports}/{@code validate} 代码可以直接复用。</p>
 */
public interface SpringBusinessValidator extends Validator {
    String key();
    default int order() { return 0; }
}
