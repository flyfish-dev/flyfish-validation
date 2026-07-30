package org.springframework.validation;
public class FieldError extends ObjectError {
    private final String field; private final Object rejectedValue;
    public FieldError(String objectName,String field,Object rejectedValue,String code,String message){super(objectName,code,message);this.field=field;this.rejectedValue=rejectedValue;}
    public String getField(){return field;} public Object getRejectedValue(){return rejectedValue;}
}
