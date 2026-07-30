package org.springframework.validation;
public class ObjectError {
    private final String objectName; private final String[] codes; private final String message;
    public ObjectError(String objectName, String code, String message) { this.objectName=objectName; this.codes=code==null?null:new String[]{code}; this.message=message; }
    public String getObjectName(){return objectName;} public String[] getCodes(){return codes==null?null:codes.clone();} public String getDefaultMessage(){return message;}
}
