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
import pisibg.ittalents.model.dto.AddressDTO;
import pisibg.ittalents.model.dto.HiddenPasswordUserDTO;
import pisibg.ittalents.model.dto.LoginUserDTO;
import pisibg.ittalents.model.dto.RegisterUserDTO;
import pisibg.ittalents.model.pojo.Address;
import pisibg.ittalents.model.pojo.User;
import org.springframework.web.bind.annotation.*;
import pisibg.ittalents.model.repository.AddressRepository;
import pisibg.ittalents.model.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.Optional;


@RestController
public class UserController extends AbstractController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private UserRepository userRepository;
    // TODO   get all orders
    @Autowired
    AddressRepository addressRepository;

    @PostMapping(value = "/users/register")
    public ResponseEntity<HiddenPasswordUserDTO> register(@RequestBody RegisterUserDTO dto, HttpSession session) {
        User user = new User(dto);
        if (!Authenticator.isEmailValid(user.getEmail())) {
            throw new InvalidCredentialException("Email should be valid");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new InvalidCredentialException("An account with this email exists.Please, log in");
        }
        String pass = user.getPassword();
        String confirmPass = dto.getConfirmationPassword();
        if (Authenticator.validateConfirmPassword(pass, confirmPass)) {
            user.setPassword(Authenticator.encodePassword(pass));
            SessionManager.logInUser(session, user);
            user.set_subscribed(true);
            userRepository.save(user);
        } else {
            throw new InvalidCredentialException("Password should be the same as confirm password ");
        }
        return new ResponseEntity<>(new HiddenPasswordUserDTO(user), HttpStatus.OK);
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

    @PutMapping("/users/unsubscribe")
    public ResponseEntity<String> unsubscribe(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You need to log in first");
        }
        userDao.unsubscribe(user.getId());
        return new ResponseEntity<>("User unsubscribed successfully!", HttpStatus.OK);
    }

    @PutMapping("/users/subscribe")
    public ResponseEntity<String> subscribe(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (!SessionManager.isLogged(session)) {
            throw new AuthorizationException("You have to log in first");
        }
        userDao.subscribe(user.getId());
        return new ResponseEntity<>("User subscribed successfully!", HttpStatus.OK);
    }

    @PostMapping("/users/{id}")
    public ResponseEntity<String> addAddress(@RequestBody AddressDTO addressDTO,
                                             @PathVariable("id") HttpSession session) {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (!SessionManager.isLogged(session)) {
            throw new AuthorizationException("You have to log in first");
        }
        Address address = new Address();
        address.setCity(addressDTO.getCity());
        address.setAddress_text(addressDTO.getAddress_text());
        address.setPostal_code(addressDTO.getPostal_code());
        address.addAddressToUser(user);
        addressRepository.save(address);
        return new ResponseEntity<>("You have added an address", HttpStatus.OK);
    }

    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public Address findAddressById(Long id) {
        Optional<Address> address = addressRepository.findById(id);
        if (address.isPresent()) {
            return address.get();
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    // TODO is this the correct way to delete?
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable("id") Long id, HttpSession session) {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (!SessionManager.isLogged(session)) {
            throw new AuthorizationException("You have to log in first");
        }
        Address address = findAddressById(id);
        address.removeAddressToUser(user);
        addressRepository.delete(address);
        return new ResponseEntity<>("You have deleted an address", HttpStatus.OK);
    }

    //TODO should I validate that the user owns the address or the url is enough
    @PutMapping("/users/{id}")
    public ResponseEntity<String> editAddress(@RequestBody AddressDTO addressDTO,
                                              @PathVariable("id") Long id, HttpSession session) {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (!SessionManager.isLogged(session)) {
            throw new AuthorizationException("You have to log in first");
        }
        Address address = findAddressById(id);
        address.setCity(addressDTO.getCity());
        address.setAddress_text(addressDTO.getAddress_text());
        address.setPostal_code(addressDTO.getPostal_code());
        address.addAddressToUser(user);
        addressRepository.save(address);
        return new ResponseEntity<>("You have edited your address", HttpStatus.OK);
    }


}



