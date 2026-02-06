package uz.uzumtech.retail_service.exception;

import org.springframework.http.HttpStatus;
import uz.uzumtech.core.constant.enums.ErrorType;

public class DuplicateUserException extends ApplicationException{
    public DuplicateUserException(String message) {
        super(10011, message, HttpStatus.CONFLICT, ErrorType.INTERNAL);
    }
}
