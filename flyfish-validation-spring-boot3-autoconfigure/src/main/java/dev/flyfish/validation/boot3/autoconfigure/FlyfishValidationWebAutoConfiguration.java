package dev.flyfish.validation.boot3.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

import dev.flyfish.validation.boot3.autoconfigure.FlyfishValidationAutoConfiguration;
import dev.flyfish.validation.boot3.web.FlyfishValidationMvcExceptionHandler;
import dev.flyfish.validation.boot3.web.FlyfishValidationWebFluxExceptionHandler;
import dev.flyfish.validation.spring.ValidationFailurePipeline;

/** 根据实际 Web 技术栈注册 MVC 或 WebFlux 统一异常处理器。 */
@AutoConfiguration(after = FlyfishValidationAutoConfiguration.class)
@ConditionalOnProperty(prefix = "flyfish.validation.web", name = "enabled",
havingValue = "true", matchIfMissing = true)
public class FlyfishValidationWebAutoConfiguration {
    @Bean @ConditionalOnMissingBean
    @ConditionalOnClass(name = "org.springframework.web.servlet.DispatcherServlet")
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public FlyfishValidationMvcExceptionHandler flyfishValidationMvcExceptionHandler(
    ValidationFailurePipeline pipeline) {
        return new FlyfishValidationMvcExceptionHandler(pipeline);
    }

    @Bean @ConditionalOnMissingBean
    @ConditionalOnClass(name = "org.springframework.web.reactive.DispatcherHandler")
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public FlyfishValidationWebFluxExceptionHandler flyfishValidationWebFluxExceptionHandler(
    ValidationFailurePipeline pipeline) {
        return new FlyfishValidationWebFluxExceptionHandler(pipeline);
    }
}
