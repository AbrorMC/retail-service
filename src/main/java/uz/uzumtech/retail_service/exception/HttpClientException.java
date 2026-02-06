package uz.uzumtech.retail_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import uz.uzumtech.retail_service.constant.enums.ErrorType;

import static uz.uzumtech.retail_service.constant.enums.Error.HTTP_CLIENT_ERROR_CODE;

public class HttpClientException extends ApplicationException {

    public HttpClientException(String message, HttpStatusCode status) {
        super(HTTP_CLIENT_ERROR_CODE.getCode(), message, HttpStatus.valueOf(status.value()), ErrorType.EXTERNAL);
    }
}
