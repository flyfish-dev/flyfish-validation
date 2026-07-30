package dev.flyfish.validation.spring;

/** 三代 Starter 共用的配置属性模型。 */
public class FlyfishValidationProperties {
    private boolean enabled = true;
    private boolean failFast;
    private boolean allowDuplicateBusinessRules;
    private boolean propagateListenerException;
    private boolean exposeRejectedValue;
    private boolean exposeConstraintAttributes = true;
    private final Web web = new Web();

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public boolean isFailFast() { return failFast; }
    public void setFailFast(boolean failFast) { this.failFast = failFast; }
    public boolean isAllowDuplicateBusinessRules() { return allowDuplicateBusinessRules; }
    public void setAllowDuplicateBusinessRules(boolean value) { allowDuplicateBusinessRules = value; }
    public boolean isPropagateListenerException() { return propagateListenerException; }
    public void setPropagateListenerException(boolean value) { propagateListenerException = value; }
    public boolean isExposeRejectedValue() { return exposeRejectedValue; }
    public void setExposeRejectedValue(boolean value) { exposeRejectedValue = value; }
    public boolean isExposeConstraintAttributes() { return exposeConstraintAttributes; }
    public void setExposeConstraintAttributes(boolean value) { exposeConstraintAttributes = value; }
    public Web getWeb() { return web; }

    /** 默认 Web Advice 的响应配置。 */
    public static class Web {
        private boolean enabled = true;
        private int status = 400;
        private String code = "VALIDATION_FAILED";
        private String message = "请求参数验证失败";
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
