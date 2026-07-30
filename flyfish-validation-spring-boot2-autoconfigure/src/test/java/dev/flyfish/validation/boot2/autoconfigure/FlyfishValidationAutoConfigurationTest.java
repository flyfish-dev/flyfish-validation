package dev.flyfish.validation.boot2.autoconfigure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

import dev.flyfish.validation.FlyfishValidator;
import dev.flyfish.validation.business.BusinessValidationExecutor;
import dev.flyfish.validation.business.BusinessValidatorRegistry;
import dev.flyfish.validation.region.AdministrativeDivisionProvider;
import dev.flyfish.validation.spring.ValidationFailurePipeline;

/** 使用真实 Spring ApplicationContext 验证 Starter 自动装配。 */
@SpringBootTest(classes = FlyfishValidationAutoConfigurationTest.TestApplication.class,
webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FlyfishValidationAutoConfigurationTest {

    @Autowired private FlyfishValidator validator;
    @Autowired private BusinessValidatorRegistry registry;
    @Autowired private BusinessValidationExecutor executor;
    @Autowired private AdministrativeDivisionProvider divisionProvider;
    @Autowired private ValidationFailurePipeline failurePipeline;

    @Test
    void shouldCreateAllPublicInfrastructureBeans() {
        assertThat(validator).isNotNull();
        assertThat(registry).isNotNull();
        assertThat(executor).isNotNull();
        assertThat(divisionProvider).isNotNull();
        assertThat(failurePipeline).isNotNull();
    }

    @SpringBootConfiguration
    @EnableAutoConfiguration
    static class TestApplication { }
}
