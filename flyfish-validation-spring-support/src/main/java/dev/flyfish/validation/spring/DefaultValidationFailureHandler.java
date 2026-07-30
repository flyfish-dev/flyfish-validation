package dev.flyfish.validation.spring;

/** 根据配置生成稳定、无堆栈的默认 4xx 响应。 */
public final class DefaultValidationFailureHandler
    implements ValidationFailureHandler {

    private final FlyfishValidationProperties properties;

    public DefaultValidationFailureHandler(
    FlyfishValidationProperties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("properties 不能为空");
        }
        this.properties = properties;
    }

    @Override
    public ValidationFailureAction handle(ValidationFailure failure) {
        FlyfishValidationProperties.Web web = properties.getWeb();
        ValidationProblem body = new ValidationProblem(web.getStatus(),
        web.getCode(), web.getMessage(), failure.getPath(),
        failure.getMethod(), failure.getReport().getErrors());
        return ValidationFailureAction.of(web.getStatus(), body);
    }
}
