package org.springframework.validation;
@SuppressWarnings("serial")
public class BindException extends Exception {
 private final transient BindingResult bindingResult;
 public BindException(BindingResult bindingResult){this.bindingResult=bindingResult;}
 public BindingResult getBindingResult(){return bindingResult;}
}
