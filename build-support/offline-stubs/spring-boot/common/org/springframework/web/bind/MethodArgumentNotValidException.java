package org.springframework.web.bind;
import org.springframework.validation.BindingResult;
@SuppressWarnings("serial")
public class MethodArgumentNotValidException extends Exception {
 private final transient BindingResult bindingResult;
 public MethodArgumentNotValidException(BindingResult bindingResult){this.bindingResult=bindingResult;}
 public BindingResult getBindingResult(){return bindingResult;}
}
