package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.FileExtension;
import dev.flyfish.validation.support.StandardChecks;

/** {@link FileExtension} 的无状态验证器。 */
public final class FileExtensionValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<FileExtension, CharSequence> {
    private String[] extensions;

    @Override
    public void initialize(FileExtension annotation) {
        extensions = annotation.value().clone();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isFileExtension(value, extensions);
    }
}
