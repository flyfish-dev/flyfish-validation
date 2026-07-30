package dev.flyfish.validation.spring;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import dev.flyfish.validation.api.ValidationError;
import dev.flyfish.validation.api.ValidationReport;
import dev.flyfish.validation.business.BusinessValidationContext;
import dev.flyfish.validation.business.BusinessValidator;

/** 把 Spring Errors 风格验证器无损适配为 Flyfish 业务规则。 */
public final class SpringBusinessValidatorAdapter
    implements BusinessValidator<Object> {
    private final SpringBusinessValidator delegate;

    public SpringBusinessValidatorAdapter(SpringBusinessValidator delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("delegate 不能为空");
        }
        this.delegate = delegate;
    }

    @Override public String key() { return delegate.key(); }
    @Override public Class<Object> targetType() { return Object.class; }
    @Override public int order() { return delegate.order(); }

    @Override
    public ValidationReport validate(Object target,
    BusinessValidationContext context) {
        if (target != null && !delegate.supports(target.getClass())) {
            return ValidationReport.invalid(ValidationError.builder(
            "SPRING_VALIDATOR_TYPE_MISMATCH",
            "Spring 业务验证器不支持当前数据类型")
                .validator(key())
                .attribute("actualType", target.getClass().getName()).build());
        }
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(
        target, target == null ? "target"
            : target.getClass().getSimpleName());
        if (delegate instanceof ContextAwareSpringBusinessValidator) {
            ((ContextAwareSpringBusinessValidator) delegate)
                .validate(target, errors, context);
        } else {
            delegate.validate(target, errors);
        }
        ValidationReport.Builder report = ValidationReport.builder();
        for (ObjectError error : errors.getAllErrors()) {
            String path = error instanceof FieldError
                ? ((FieldError) error).getField() : "";
            Object rejected = error instanceof FieldError
                ? ((FieldError) error).getRejectedValue() : null;
            report.error(ValidationError.builder(firstCode(error),
            error.getDefaultMessage() == null
                ? "业务校验失败" : error.getDefaultMessage())
                .propertyPath(path).validator(key())
                .rejectedValue(rejected).build());
        }
        return report.build();
    }

    private static String firstCode(ObjectError error) {
        String[] codes = error.getCodes();
        return codes == null || codes.length == 0
            ? "BUSINESS_VALIDATION_FAILED" : codes[0];
    }
}
