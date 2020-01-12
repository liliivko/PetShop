package pisibg.ittalents.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (HttpStatus.BAD_REQUEST)

public class InvalidPaymentMethodException extends RuntimeException {

    public InvalidPaymentMethodException(String msg) {
        super(msg);
    }
}
