# MVC 与 WebFlux 统一错误处理

## 覆盖异常

- 请求体/参数绑定错误；
- Bean Validation `ConstraintViolationException`；
- 程序式 `ValidationException`；
- Spring Framework 6.1+ 方法验证异常；
- MVC 与 WebFlux 对应的绑定异常形态。

## 默认安全策略

`DefaultValidationRejectedValueSanitizer` 默认删除 rejected value。原因是绑定错误可能包含密码、身份证、银行卡、Token、Cookie 或业务密钥。即使日志系统有脱敏，也不应把原值先放进公网响应。

约束 attributes 默认保留，便于前端获得 `min`、`max` 等参数；对安全敏感注解可关闭，或提供自定义 Sanitizer 仅保留白名单。

## 自定义错误码

实现 `ValidationErrorCustomizer`：

```java
@Component
public final class ApiErrorCodeCustomizer
        implements ValidationErrorCustomizer {
    @Override public int order() { return 100; }

    @Override
    public ValidationError customize(ValidationError source,
                                     Throwable cause) {
        return ValidationError.builder(
                        "PARAM_" + source.getCode(), source.getMessage())
                .propertyPath(source.getPropertyPath())
                .severity(source.getSeverity())
                .validator(source.getValidator())
                .attributes(source.getAttributes())
                .build();
    }
}
```

## 自定义响应

实现 `ValidationFailureHandler`，返回 `ValidationFailureAction` 即可控制状态、Header 和 body。也可以关闭 `flyfish.validation.web.enabled`，由公司的全局异常框架直接处理 `ValidationException` 与标准 Spring 异常。

默认 Advice 使用最低优先级，业务系统中更高优先级的 Advice 可自然覆盖。
