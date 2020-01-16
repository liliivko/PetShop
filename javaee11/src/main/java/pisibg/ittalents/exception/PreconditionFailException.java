package pisibg.ittalents.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class PreconditionFailException extends Exception {

    public PreconditionFailException(String msg) {
        super(msg);
    }
}
