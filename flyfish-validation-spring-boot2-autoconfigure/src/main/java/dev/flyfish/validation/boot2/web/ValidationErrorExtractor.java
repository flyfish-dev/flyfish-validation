package dev.flyfish.validation.boot2.web;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import dev.flyfish.validation.api.ValidationError;
import dev.flyfish.validation.api.ValidationException;

/**
 * 将 Bean Validation、Spring Binding 与方法验证异常归一化为公共错误模型。
 *
 * <p>对 Spring 6.1+ 的方法验证结果使用反射读取，避免公共提取逻辑绑定某个
 * Spring Framework 小版本的结果接口。</p>
 */
public final class ValidationErrorExtractor {

    private ValidationErrorExtractor() { }

    public static List<ValidationError> extract(Throwable cause) {
        if (cause instanceof ValidationException) {
            return ((ValidationException) cause).getReport().getErrors();
        }
        if (cause instanceof MethodArgumentNotValidException) {
            return fromBindingResult(
            ((MethodArgumentNotValidException) cause)
                .getBindingResult());
        }
        if (cause instanceof BindException) {
            return fromBindingResult(
            ((BindException) cause).getBindingResult());
        }
        if (cause instanceof ConstraintViolationException) {
            List<ValidationError> errors = new ArrayList<ValidationError>();
            for (ConstraintViolation<?> violation
                : ((ConstraintViolationException) cause)
                .getConstraintViolations()) {
                errors.add(fromViolation(violation));
            }
            return sorted(errors);
        }
        if (isType(cause,
        "org.springframework.web.method.annotation."
        + "HandlerMethodValidationException")
            || isType(cause,
        "org.springframework.validation.method."
        + "MethodValidationException")) {
            List<ValidationError> reflected =
            extractMethodValidation(cause);
            if (!reflected.isEmpty()) {
                return reflected;
            }
        }
        return Collections.singletonList(ValidationError.builder(
        "VALIDATION_FAILED", defaultMessage(cause))
            .build());
    }

    public static List<ValidationError> fromBindingResult(
    BindingResult result) {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if (result == null) {
            return errors;
        }
        for (ObjectError error : result.getAllErrors()) {
            if (error instanceof FieldError) {
                FieldError field = (FieldError) error;
                errors.add(ValidationError.builder(
                firstCode(field), message(field))
                    .propertyPath(field.getField())
                    .rejectedValue(field.getRejectedValue())
                    .build());
            } else {
                errors.add(ValidationError.builder(
                firstCode(error), message(error))
                    .propertyPath(error.getObjectName())
                    .build());
            }
        }
        return sorted(errors);
    }

    private static ValidationError fromViolation(
    ConstraintViolation<?> violation) {
        Map<String, Object> attributes =
        new LinkedHashMap<String, Object>();
        String validatorName = "BeanValidation";
        if (violation.getConstraintDescriptor() != null) {
            attributes.putAll(violation.getConstraintDescriptor()
                .getAttributes());
            attributes.remove("message");
            attributes.remove("groups");
            attributes.remove("payload");
            if (violation.getConstraintDescriptor().getAnnotation() != null) {
                validatorName = violation.getConstraintDescriptor()
                    .getAnnotation().annotationType().getSimpleName();
            }
        }
        return ValidationError.builder(validatorName,
        violation.getMessage())
            .propertyPath(violation.getPropertyPath() == null
            ? "" : violation.getPropertyPath().toString())
            .validator(validatorName)
            .rejectedValue(violation.getInvalidValue())
            .attributes(attributes)
            .build();
    }

    private static List<ValidationError> extractMethodValidation(
    Throwable cause) {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        Object results = invoke(cause, "getAllValidationResults");
        if (results == null) {
            results = invoke(cause, "getParameterValidationResults");
        }
        for (Object result : iterable(results)) {
            String path = parameterName(result);
            Object resolvableErrors = invoke(result, "getResolvableErrors");
            for (Object value : iterable(resolvableErrors)) {
                if (value instanceof ObjectError) {
                    ObjectError error = (ObjectError) value;
                    errors.add(ValidationError.builder(
                    firstCode(error), message(error))
                        .propertyPath(error instanceof FieldError
                        ? ((FieldError) error).getField() : path)
                        .rejectedValue(invoke(result, "getArgument"))
                        .build());
                } else if (value != null) {
                    Object defaultMessage =
                    invoke(value, "getDefaultMessage");
                    errors.add(ValidationError.builder(
                    firstReflectiveCode(
                    invoke(value, "getCodes")),
                    defaultMessage == null
                        ? "方法参数校验失败"
                        : defaultMessage.toString())
                        .propertyPath(path)
                        .rejectedValue(invoke(result, "getArgument"))
                        .build());
                }
            }
        }
        return sorted(errors);
    }

    private static String parameterName(Object result) {
        Object parameter = invoke(result, "getMethodParameter");
        Object name = invoke(parameter, "getParameterName");
        if (name != null) {
            return name.toString();
        }
        Object index = invoke(parameter, "getParameterIndex");
        return index == null ? "method" : "arg" + index;
    }

    private static String firstCode(ObjectError error) {
        String[] codes = error.getCodes();
        return codes == null || codes.length == 0
            ? "VALIDATION_FAILED" : codes[0];
    }

    private static String firstReflectiveCode(Object codes) {
        if (codes != null && codes.getClass().isArray()
            && Array.getLength(codes) > 0) {
            Object first = Array.get(codes, 0);
            return first == null
                ? "VALIDATION_FAILED" : first.toString();
        }
        return "VALIDATION_FAILED";
    }

    private static String message(ObjectError error) {
        return error.getDefaultMessage() == null
            ? "校验失败" : error.getDefaultMessage();
    }

    private static String defaultMessage(Throwable cause) {
        return cause == null || cause.getMessage() == null
            ? "请求参数验证失败" : cause.getMessage();
    }

    private static List<ValidationError> sorted(
    List<ValidationError> errors) {
        Collections.sort(errors, new Comparator<ValidationError>() {
            @Override
            public int compare(ValidationError left,
            ValidationError right) {
                int path = left.getPropertyPath()
                    .compareTo(right.getPropertyPath());
                return path == 0
                    ? left.getCode().compareTo(right.getCode()) : path;
            }
        });
        return errors;
    }

    private static boolean isType(Throwable cause, String name) {
        return cause != null && cause.getClass().getName().equals(name);
    }

    private static Iterable<?> iterable(Object value) {
        return value instanceof Iterable<?>
            ? (Iterable<?>) value : Collections.emptyList();
    }

    private static Object invoke(Object target, String methodName) {
        if (target == null) {
            return null;
        }
        try {
            Method method = target.getClass().getMethod(methodName);
            return method.invoke(target);
        } catch (ReflectiveOperationException exception) {
            return null;
        }
    }
}
