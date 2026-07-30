package dev.flyfish.validation.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/** 一次验证产生的不可变错误报告。 */
public final class ValidationReport {
    private static final ValidationReport VALID =
    new ValidationReport(Collections.<ValidationError>emptyList());
    private final List<ValidationError> errors;

    private ValidationReport(Collection<ValidationError> values) {
        List<ValidationError> copy = new ArrayList<ValidationError>();
        if (values != null) {
            for (ValidationError value : values) {
                if (value != null) { copy.add(value); }
            }
        }
        this.errors = Collections.unmodifiableList(copy);
    }

    public static ValidationReport valid() { return VALID; }
    public static ValidationReport invalid(ValidationError error) {
        return error == null ? VALID : new ValidationReport(
        Collections.singletonList(error));
    }
    public static ValidationReport of(Collection<ValidationError> errors) {
        return errors == null || errors.isEmpty() ? VALID : new ValidationReport(errors);
    }
    public static Builder builder() { return new Builder(); }
    public boolean isValid() { return errors.isEmpty(); }
    public List<ValidationError> getErrors() { return errors; }
    public ValidationReport merge(ValidationReport other) {
        if (other == null || other.isValid()) { return this; }
        if (isValid()) { return other; }
        List<ValidationError> merged = new ArrayList<ValidationError>(errors);
        merged.addAll(other.errors);
        return new ValidationReport(merged);
    }

    /** 聚合多个验证器结果的构建器。 */
    public static final class Builder {
        private final List<ValidationError> errors = new ArrayList<ValidationError>();
        private Builder() { }
        public Builder error(ValidationError value) {
            if (value != null) { errors.add(value); }
            return this;
        }
        public Builder errors(Collection<ValidationError> values) {
            if (values != null) {
                for (ValidationError value : values) { error(value); }
            }
            return this;
        }
        public Builder report(ValidationReport value) {
            return value == null ? this : errors(value.getErrors());
        }
        public ValidationReport build() { return ValidationReport.of(errors); }
    }
}
