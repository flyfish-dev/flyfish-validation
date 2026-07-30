package dev.flyfish.validation;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.metadata.ConstraintDescriptor;

import dev.flyfish.validation.api.ValidationError;

/** 把 Provider 约束错误转换为与框架无关的统一错误。 */
final class BeanValidationErrorMapper {
    private BeanValidationErrorMapper() { }
    static ValidationError map(ConstraintViolation<?> violation) {
        ConstraintDescriptor<?> descriptor = violation.getConstraintDescriptor();
        Annotation annotation = descriptor == null ? null : descriptor.getAnnotation();
        String validator = annotation == null ? "BeanValidation"
            : annotation.annotationType().getSimpleName();
        Map<String,Object> attributes = new LinkedHashMap<String,Object>();
        if (descriptor != null && descriptor.getAttributes() != null) {
            for (Map.Entry<String,Object> entry : descriptor.getAttributes().entrySet()) {
                String key = entry.getKey();
                if (!"message".equals(key) && !"groups".equals(key)
                    && !"payload".equals(key)) {
                    attributes.put(key, safe(entry.getValue()));
                }
            }
        }
        return ValidationError.builder(validator, violation.getMessage())
            .propertyPath(violation.getPropertyPath() == null ? ""
            : violation.getPropertyPath().toString())
            .validator(validator)
            .rejectedValue(violation.getInvalidValue())
            .attributes(attributes).build();
    }
    private static Object safe(Object value) {
        if (value == null || value instanceof Number || value instanceof Boolean
            || value instanceof CharSequence || value instanceof Enum<?>) { return value; }
        if (value instanceof Class<?>) { return ((Class<?>) value).getName(); }
        if (value.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(value);
            java.util.List<Object> values = new java.util.ArrayList<Object>();
            for (int index=0; index<Math.min(length,64); index++) {
                values.add(safe(java.lang.reflect.Array.get(value,index)));
            }
            return values;
        }
        return value.toString();
    }
}
