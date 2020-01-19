package pw.react.flatly.flatlybackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ParamsMismatchException extends RuntimeException {
    public ParamsMismatchException(String message) {
        super(message);
    }

    public ParamsMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
