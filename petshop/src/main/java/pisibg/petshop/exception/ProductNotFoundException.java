package pisibg.petshop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such product")
public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String s) {
    }
}
