package pisibg.ittalents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import pisibg.ittalents.SessionManager;
import pisibg.ittalents.exception.InvalidCredentialException;
import pisibg.ittalents.exception.UserNotFoundException;
import pisibg.ittalents.model.dto.HiddenPasswordUserDTO;
import pisibg.ittalents.model.dto.LoginUserDTO;
import pisibg.ittalents.model.dto.RegisterUserDTO;
import pisibg.ittalents.model.pojo.User;
import org.springframework.web.bind.annotation.*;
import pisibg.ittalents.model.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/users/register")
    // unique emeil only
    public HiddenPasswordUserDTO register(@RequestBody RegisterUserDTO dto, HttpSession session) {
        // validate if the user exists and password validation
        User user = new User(dto);
        // check if this is valid way of encryption
        String pass = user.getPassword();
        user.setPassword(Authenticator.encodePassword(pass));
        SessionManager.logInUser(session, user);
        userRepository.save(user);
        return new HiddenPasswordUserDTO(user);
    }

    @GetMapping(value = "/users")
    public List<User> getAllUsers(@RequestBody User user) {
        if (user.is_admin()) {
            return userRepository.findAll();
        }
        return null;
    }

    @PostMapping("/users/login")
    public ResponseEntity<HiddenPasswordUserDTO> login(@RequestBody LoginUserDTO loginUser, HttpSession session) {
        User user = userRepository.findByEmail(loginUser.getEmail());
        if (user == null) {
            throw new UserNotFoundException("Invalid user name or password. Please, try again!");
        } else if (Authenticator.passwordIsAuthenticated(loginUser, user)) {
            if (session.isNew()) {
                SessionManager.logInUser(session, user);
            }
            return new ResponseEntity<>(new HiddenPasswordUserDTO(user), HttpStatus.OK);
        } else {
            throw new InvalidCredentialException("Invalid credentials");
        }
    }

    @PostMapping("/users/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return new ResponseEntity<>("You have logged out", HttpStatus.OK);
    }

    @DeleteMapping("/users/delete")
    // should i use user or hiddenpass user
    public ResponseEntity<String> deleteUser(@RequestBody User user, HttpSession session) {
        if (SessionManager.isLogged(session)) {
            userRepository.deleteById(user.getId());
            session.invalidate();
        }
        return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
    }

   // TODO  @PutMapping("users/subscribe"), unsubscribe, get all orders


    @ExceptionHandler(SQLException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleSQLException() {
        return "Sorry, something went wrong.Try again later";
    }

    @ExceptionHandler(InvalidCredentialException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleInvalidException() {
        return "Invalid credentials. Please, try again";
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleUserNotFoundException() {
        return "You need to log in first";
    }

}




