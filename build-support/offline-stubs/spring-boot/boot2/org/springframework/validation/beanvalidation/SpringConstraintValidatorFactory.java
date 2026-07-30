package org.springframework.validation.beanvalidation;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
public class SpringConstraintValidatorFactory implements javax.validation.ConstraintValidatorFactory {
 public SpringConstraintValidatorFactory(AutowireCapableBeanFactory beanFactory) { }
 public <T extends javax.validation.ConstraintValidator<?,?>> T getInstance(Class<T> key){return null;}
 public void releaseInstance(javax.validation.ConstraintValidator<?,?> instance){ }
}
