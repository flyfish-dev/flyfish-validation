package org.springframework.web.bind.support;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import java.util.List;
@SuppressWarnings("serial")
public class WebExchangeBindException extends Exception implements BindingResult {
 private final transient BindingResult delegate;
 public WebExchangeBindException(BindingResult delegate){this.delegate=delegate;}
 public void reject(String code,String message){delegate.reject(code,message);}
 public void rejectValue(String field,String code,String message){delegate.rejectValue(field,code,message);}
 public List<ObjectError> getAllErrors(){return delegate.getAllErrors();}
}
