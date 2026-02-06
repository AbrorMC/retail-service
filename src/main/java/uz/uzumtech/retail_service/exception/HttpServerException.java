package uz.uzumtech.retail_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import uz.uzumtech.core.constant.enums.ErrorType;

import static uz.uzumtech.core.constant.enums.Error.HTTP_SERVICE_ERROR_CODE;

public class HttpServerException extends ApplicationException {

    public HttpServerException(String message, HttpStatusCode status) {
        super(HTTP_SERVICE_ERROR_CODE.getCode(), message, HttpStatus.valueOf(status.value()), ErrorType.EXTERNAL);
    }
}
