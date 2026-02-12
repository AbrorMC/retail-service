package uz.uzumtech.retail_service.exception;

import org.springframework.http.HttpStatus;
import uz.uzumtech.retail_service.constant.enums.ErrorType;

public class OrderNotFoundException extends ApplicationException{
    public OrderNotFoundException(String message) {
        super(10011, message, HttpStatus.NOT_FOUND, ErrorType.INTERNAL);
    }
}
