package dev.flyfish.validation.business;

/** 请求执行的业务验证器未注册。 */
public final class MissingBusinessValidatorException extends IllegalStateException {
    private static final long serialVersionUID = 1L;
    public MissingBusinessValidatorException(String key) {
        super("未注册业务验证规则: " + key);
    }
}
