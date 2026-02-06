package uz.uzumtech.retail_service.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import uz.uzumtech.retail_service.constant.enums.ErrorType;

@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationException extends RuntimeException {
    int code;
    String message;
    HttpStatus status;
    ErrorType errorType;

    public ApplicationException(int code, String message, HttpStatus status, ErrorType errorType) {
        super(message);
        this.code = code;
        this.message = message;
        this.status = status;
        this.errorType = errorType;
    }
}
