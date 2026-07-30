package dev.flyfish.validation.boot4.web;

import java.util.Map;

import jakarta.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.ServerWebExchange;

import dev.flyfish.validation.api.ValidationException;
import dev.flyfish.validation.spring.ValidationFailureAction;
import dev.flyfish.validation.spring.ValidationFailurePipeline;

/** Spring WebFlux 统一验证异常处理器。 */
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public final class FlyfishValidationWebFluxExceptionHandler {

    private final ValidationFailurePipeline pipeline;

    public FlyfishValidationWebFluxExceptionHandler(
    ValidationFailurePipeline pipeline) {
        this.pipeline = pipeline;
    }

    @ExceptionHandler({WebExchangeBindException.class,
        ConstraintViolationException.class, ValidationException.class,
        HandlerMethodValidationException.class,
        MethodValidationException.class})
    public ResponseEntity<Object> handle(Exception exception,
    ServerWebExchange exchange) {
        ValidationFailureAction action = pipeline.handle(exception,
        exception instanceof WebExchangeBindException
            ? ValidationErrorExtractor.fromBindingResult(
        (WebExchangeBindException) exception)
            : ValidationErrorExtractor.extract(exception),
        exchange.getRequest().getPath().value(),
        exchange.getRequest().getMethod() == null
            ? "" : exchange.getRequest().getMethod().name());
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
