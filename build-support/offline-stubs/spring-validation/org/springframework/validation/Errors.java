package org.springframework.validation;
import java.util.List;
public interface Errors {
    void reject(String code, String defaultMessage);
    void rejectValue(String field, String code, String defaultMessage);
    List<ObjectError> getAllErrors();
}
