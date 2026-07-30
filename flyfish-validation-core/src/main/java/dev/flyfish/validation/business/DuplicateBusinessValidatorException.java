package dev.flyfish.validation.business;

/** 同名业务验证器冲突。 */
public final class DuplicateBusinessValidatorException extends IllegalStateException {
    private static final long serialVersionUID = 1L;
    public DuplicateBusinessValidatorException(String key) {
        super("发现重复业务验证规则: " + key);
    }
}
