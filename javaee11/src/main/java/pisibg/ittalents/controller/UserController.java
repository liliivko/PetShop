package pisibg.ittalents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pisibg.ittalents.SessionManager;
import pisibg.ittalents.dao.AddressDao;
import pisibg.ittalents.dao.UserDao;
import pisibg.ittalents.exception.AuthorizationException;
import pisibg.ittalents.exception.InvalidCredentialException;
import pisibg.ittalents.exception.UserNotFoundException;
import pisibg.ittalents.model.dto.HiddenPasswordUserDTO;
import pisibg.ittalents.model.dto.LoginUserDTO;
import pisibg.ittalents.model.dto.RegisterUserDTO;
import pisibg.ittalents.model.pojo.Address;
import pisibg.ittalents.model.pojo.User;
import org.springframework.web.bind.annotation.*;
import pisibg.ittalents.model.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;


@RestController
public class UserController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private UserRepository userRepository;
// TODO   get all orders, ratings

    @PostMapping(value = "/users/register")
    public ResponseEntity<HiddenPasswordUserDTO> register(@RequestBody RegisterUserDTO dto, HttpSession session) {
        User user = new User(dto);
        String pass = user.getPassword();
        user.setPassword(Authenticator.encodePassword(pass));
        SessionManager.logInUser(session, user);
        userRepository.save(user);
        return new ResponseEntity<>(new HiddenPasswordUserDTO(user), HttpStatus.OK);
    }

    @GetMapping(value = "/users/all")
    public List<User> getAllUsers(HttpSession session) {
        List<User> users = null;
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You have to log in");
        }
        if (user.is_admin()) {
            users = userRepository.findAll();
            return users;
        } else {
            throw new AuthorizationException("You are not authorized");
        }
    }

    @PostMapping("/users/login")
    public HiddenPasswordUserDTO login(@RequestBody LoginUserDTO loginUser, HttpSession session) {
        User user = userRepository.findByEmail(loginUser.getEmail());
        if (user == null) {
            throw new UserNotFoundException("Invalid user name or password. Please, try again!");
        }
        if (Authenticator.passwordIsAuthenticated(loginUser, user)) {
            SessionManager.logInUser(session, user);
            return new HiddenPasswordUserDTO(user);
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
    public ResponseEntity<String> deleteUser(@RequestBody User user, HttpSession session) {
        if (SessionManager.isLogged(session)) {
            userRepository.deleteById(user.getId());
            session.invalidate();
        }
        return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
    }


    @PutMapping("/users/unsubscribe")
    public ResponseEntity<String> unsubscribe(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You need to log in first");
        }
        userDao.unsubscribe(user);
        return new ResponseEntity<>("User unsubscribed successfully!", HttpStatus.OK);
    }

    @PutMapping("/users/subscribe")
    public ResponseEntity<String> subscribe(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (!SessionManager.isLogged(session)) {
            throw new AuthorizationException("You have to log in first");
        }
        userDao.subscribe(user);
        return new ResponseEntity<>("User subscribed successfully!", HttpStatus.OK);
    }

    @PostMapping("/users/addresses")
    public ResponseEntity<String> addAddress(@RequestBody Address address, HttpSession session) throws SQLException {
        // add address, connect it to user
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (!SessionManager.isLogged(session)) {
            throw new AuthorizationException("You have to log in first");
        }
        addressDao.saveAddress(address);
        addressDao.addAddress(user,address);
        return new ResponseEntity<>("You have added an address", HttpStatus.OK);
    }


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




