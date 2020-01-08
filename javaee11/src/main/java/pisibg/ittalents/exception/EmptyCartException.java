package pisibg.ittalents.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Empty cart")
public class EmptyCartException extends RuntimeException {

    public EmptyCartException(String msg) {
        super(msg);
    }
}
