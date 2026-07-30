package javax.validation; public interface ConstraintValidatorFactory { <T extends ConstraintValidator<?,?>> T getInstance(Class<T> key); void releaseInstance(ConstraintValidator<?,?> instance); }
