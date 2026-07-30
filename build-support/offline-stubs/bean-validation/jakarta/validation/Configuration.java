package jakarta.validation; public interface Configuration<T extends Configuration<T>> { T constraintValidatorFactory(ConstraintValidatorFactory factory); T addProperty(String name,String value); }
