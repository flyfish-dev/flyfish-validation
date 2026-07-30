package org.springframework.validation.beanvalidation;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
public class SpringConstraintValidatorFactory implements jakarta.validation.ConstraintValidatorFactory {
 public SpringConstraintValidatorFactory(AutowireCapableBeanFactory beanFactory) { }
 public <T extends jakarta.validation.ConstraintValidator<?,?>> T getInstance(Class<T> key){return null;}
 public void releaseInstance(jakarta.validation.ConstraintValidator<?,?> instance){ }
}
