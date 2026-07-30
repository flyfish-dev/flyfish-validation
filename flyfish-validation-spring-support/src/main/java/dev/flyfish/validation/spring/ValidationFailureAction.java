package dev.flyfish.validation.spring;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** 与 MVC/WebFlux 无关的 HTTP 响应决策。 */
public final class ValidationFailureAction {
    private final int status;
    private final Map<String,String> headers;
    private final Object body;
    public ValidationFailureAction(int status, Map<String,String> headers,
    Object body) {
        this.status = status;
        this.headers = Collections.unmodifiableMap(headers == null
            ? new LinkedHashMap<String,String>()
            : new LinkedHashMap<String,String>(headers));
        this.body = body;
    }
    public static ValidationFailureAction of(int status, Object body) {
        return new ValidationFailureAction(status,
        Collections.<String,String>emptyMap(), body);
    }
    public int getStatus() { return status; }
    public Map<String,String> getHeaders() { return headers; }
    public Object getBody() { return body; }
}
