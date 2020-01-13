package pisibg.ittalents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pisibg.ittalents.SessionManager;
import pisibg.ittalents.dao.UserDAO;
import pisibg.ittalents.exception.AuthorizationException;
import pisibg.ittalents.exception.NotFoundException;
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
    private UserDAO userDao;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    AddressRepository addressRepository;

    @PostMapping(value = "/users/register")
    //TODO trim
    public ResponseEntity<HiddenPasswordUserDTO> register(@RequestBody RegisterUserDTO dto, HttpSession session) {
        User user = new User(dto);
        if (!Authenticator.isEmailValid(user.getEmail())) {
            throw new AuthorizationException("Email should be valid");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new AuthorizationException("An account with this email exists.Please, log in");
        }
        String pass = user.getPassword();
        String confirmPass = dto.getConfirmationPassword();
        if (Authenticator.validateConfirmPassword(pass, confirmPass)) {
            user.setPassword(Authenticator.encodePassword(pass));
            SessionManager.logInUser(session, user);
            user.set_subscribed(true);
            userRepository.save(user);
        } else {
            throw new AuthorizationException("Password should be the same as confirm password ");
        }
        return new ResponseEntity<>(new HiddenPasswordUserDTO(user), HttpStatus.OK);
    }

    @PostMapping("/users/login")
    public HiddenPasswordUserDTO login(@RequestBody LoginUserDTO loginUser, HttpSession session) {
        User user = userRepository.findByEmail(loginUser.getEmail());
        if (user == null) {
            throw new NotFoundException("Invalid user name or password. Please, try again!");
        }
        if (Authenticator.passwordIsAuthenticated(loginUser, user)) {
            SessionManager.logInUser(session, user);
            return new HiddenPasswordUserDTO(user);
        } else {
            throw new AuthorizationException("Invalid credentials");
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
        if (user.is_subscribed()) {
            userDao.unsubscribe(user.getId());
        }
        return new ResponseEntity<>("User unsubscribed successfully!", HttpStatus.OK);
    }

    @PutMapping("/users/subscribe")
    public ResponseEntity<String> subscribe(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (!SessionManager.isLogged(session)) {
            throw new AuthorizationException("You have to log in first");
        }
        if (user.is_subscribed()) {
            throw new AuthorizationException("You are already subscribed");
        }
        userDao.subscribe(user.getId());
        return new ResponseEntity<>("User subscribed successfully!", HttpStatus.OK);
    }

    @PostMapping("/users/{id}")//TODO
    public ResponseEntity<String> addAddress(@RequestBody AddressDTO addressDTO,
                                             @PathVariable("id") Long id, HttpSession session) {
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


    public Address findAddressById(Long id) {
        Optional<Address> address = addressRepository.findById(id);
        if (address.isPresent()) {
            return address.get();
        } else {
            throw new NotFoundException("User not found");
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable("id") Long id, HttpSession session) {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (!SessionManager.isLogged(session)) {
            throw new AuthorizationException("You have to log in first");
        }
        Address address = findAddressById(id);
        if (!user.getAddresses().contains(address)) {
            throw new AuthorizationException("Yoy are not authorized delete this address");
        }
        address.removeAddressToUser(user);
        addressRepository.delete(address);
        return new ResponseEntity<>("You have deleted an address", HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<String> editAddress(@RequestBody AddressDTO addressDTO,
                                              @PathVariable("id") Long id, HttpSession session) {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (!SessionManager.isLogged(session)) {
            throw new AuthorizationException("You have to log in first");
        }
        Address address = findAddressById(id);
        if (!user.getAddresses().contains(address)) {
            throw new AuthorizationException("Yoy are not authorized delete this address");
        }
        address.setCity(addressDTO.getCity());
        address.setAddress_text(addressDTO.getAddress_text());
        address.setPostal_code(addressDTO.getPostal_code());
        address.addAddressToUser(user);
        addressRepository.save(address);
        return new ResponseEntity<>("You have edited your address", HttpStatus.OK);
    }
}



