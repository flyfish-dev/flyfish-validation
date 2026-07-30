package org.springframework.validation.beanvalidation;
import java.util.Map;
import java.util.Set;
public class LocalValidatorFactoryBean implements javax.validation.Validator {
    public void setConstraintValidatorFactory(
            javax.validation.ConstraintValidatorFactory factory) { }
    public void setValidationPropertyMap(Map<String, String> properties) { }
    public <T> Set<javax.validation.ConstraintViolation<T>> validate(
            T object, Class<?>... groups) { return null; }
    public <T> Set<javax.validation.ConstraintViolation<T>> validateProperty(
            T object, String propertyName, Class<?>... groups) { return null; }
    public <T> Set<javax.validation.ConstraintViolation<T>> validateValue(
            Class<T> beanType, String propertyName, Object value,
            Class<?>... groups) { return null; }
}
