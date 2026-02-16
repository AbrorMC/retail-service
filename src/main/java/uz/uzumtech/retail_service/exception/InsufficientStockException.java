package uz.uzumtech.retail_service.exception;

import org.springframework.http.HttpStatus;
import uz.uzumtech.retail_service.constant.enums.ErrorType;

public class InsufficientStockException extends ApplicationException{
    public InsufficientStockException(String message) {
        super(10011, message, HttpStatus.BAD_REQUEST, ErrorType.INTERNAL);
    }
}
