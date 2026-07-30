package dev.flyfish.validation.example.boot4;

import org.springframework.stereotype.Component;

import dev.flyfish.validation.api.ValidationError;
import dev.flyfish.validation.api.ValidationReport;
import dev.flyfish.validation.business.BusinessValidationContext;
import dev.flyfish.validation.business.BusinessValidator;

/** 数据库关联规则：Bean 自动被 Starter 注册，不需要额外配置。 */
@Component
public final class UsernameUniqueValidator
    implements BusinessValidator<RegisterCommand> {
    private final UserRepository repository;

    public UsernameUniqueValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override public String key() { return "username-unique"; }
    @Override public Class<RegisterCommand> targetType() {
        return RegisterCommand.class;
    }

    @Override
    public ValidationReport validate(RegisterCommand value,
    BusinessValidationContext context) {
        if (!repository.existsByUsername(value.getUsername())) {
            return ValidationReport.valid();
        }
        return ValidationReport.invalid(ValidationError.builder(
        "USERNAME_TAKEN", "用户名已存在")
            .propertyPath("username")
            .validator(key())
            .build());
    }
}
