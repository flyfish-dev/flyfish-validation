package dev.flyfish.validation.boot2.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.flyfish.validation.api.ValidationException;
import dev.flyfish.validation.spring.ValidationFailureAction;
import dev.flyfish.validation.spring.ValidationFailurePipeline;

/** Spring MVC 统一验证异常处理器，使用最低优先级以便业务 Advice 覆盖。 */
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public final class FlyfishValidationMvcExceptionHandler {

    private final ValidationFailurePipeline pipeline;

    public FlyfishValidationMvcExceptionHandler(
    ValidationFailurePipeline pipeline) {
        this.pipeline = pipeline;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class,
        BindException.class, ConstraintViolationException.class,
        ValidationException.class})
    public ResponseEntity<Object> handle(Exception exception,
    HttpServletRequest request) {
        ValidationFailureAction action = pipeline.handle(exception,
        ValidationErrorExtractor.extract(exception),
        request.getRequestURI(), request.getMethod());
        return response(action);
    }

    private static ResponseEntity<Object> response(
    ValidationFailureAction action) {
        HttpHeaders headers = new HttpHeaders();
        for (Map.Entry<String, String> entry
            : action.getHeaders().entrySet()) {
            headers.add(entry.getKey(), entry.getValue());
        }
        return ResponseEntity.status(action.getStatus())
            .headers(headers).body(action.getBody());
    }
}
