package org.springframework.validation;
import java.util.ArrayList; import java.util.Collections; import java.util.List;
public class BeanPropertyBindingResult implements Errors {
    private final Object target; private final String objectName; private final List<ObjectError> errors=new ArrayList<ObjectError>();
    public BeanPropertyBindingResult(Object target,String objectName){this.target=target;this.objectName=objectName;}
    public void reject(String code,String message){errors.add(new ObjectError(objectName,code,message));}
    public void rejectValue(String field,String code,String message){errors.add(new FieldError(objectName,field,null,code,message));}
    public List<ObjectError> getAllErrors(){return Collections.unmodifiableList(errors);}
    public Object getTarget(){return target;}
}
