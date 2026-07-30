package dev.flyfish.validation.boot3.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import dev.flyfish.validation.spring.FlyfishValidationProperties;

/**
 * Flyfish Validation 的 Spring Boot 配置属性绑定入口。
 *
 * <p>该类型按 Spring Boot 主版本独立声明，避免公共 Spring 支持模块直接依赖
 * 任一特定版本的 Spring Boot API。</p>
 */
@ConfigurationProperties(prefix = "flyfish.validation")
public class FlyfishValidationConfigurationProperties extends FlyfishValidationProperties {
}
