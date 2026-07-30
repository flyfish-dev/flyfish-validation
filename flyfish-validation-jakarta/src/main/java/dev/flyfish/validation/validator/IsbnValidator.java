package dev.flyfish.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dev.flyfish.validation.constraints.Isbn;
import dev.flyfish.validation.support.StandardChecks;

/** {@link Isbn} 的无状态验证器。 */
public final class IsbnValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<Isbn, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (nullable(value)) { return true; }
        return StandardChecks.isIsbn(value);
    }
}
