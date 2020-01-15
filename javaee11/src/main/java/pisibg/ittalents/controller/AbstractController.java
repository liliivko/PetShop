package pisibg.ittalents.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pisibg.ittalents.exception.AuthorizationException;
import pisibg.ittalents.exception.EmptyCartException;
import pisibg.ittalents.model.dto.ErrorDTO;

import java.sql.SQLException;
import java.time.LocalDateTime;

public abstract class AbstractController {

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDTO handleSQLException(Exception e) {
        ErrorDTO errorDTO = new ErrorDTO(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                e.getClass().getName());
        return errorDTO;
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleInvalidException(Exception e) {
        ErrorDTO errorDTO = new ErrorDTO(
                e.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now(),
                e.getClass().getName());
        return errorDTO;
    }

    @ExceptionHandler(EmptyCartException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleEmptycart(Exception e){
        ErrorDTO errorDTO = new ErrorDTO(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                e.getClass().getName());
        return errorDTO;
    }

    // not found


}
