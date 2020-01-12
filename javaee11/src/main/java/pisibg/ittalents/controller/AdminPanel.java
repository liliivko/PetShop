package pisibg.ittalents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pisibg.ittalents.SessionManager;
import pisibg.ittalents.dao.AddressDao;
import pisibg.ittalents.dao.UserDao;
import pisibg.ittalents.exception.AuthorizationException;
import pisibg.ittalents.model.pojo.User;
import pisibg.ittalents.model.repository.AddressRepository;
import pisibg.ittalents.model.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
public class AdminPanel extends AbstractController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    AddressRepository addressRepository;

    @GetMapping(value = "/users/all")
    public List<User> getAllUsers(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You have to log in");
        }
        if (userDao.is_admin(user)) {
            return userRepository.findAll();
        } else {
            throw new AuthorizationException("You are not authorized");
        }
    }

    @DeleteMapping("/users/delete")
    public ResponseEntity<String> deleteUser(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You need to log in first");
        }
        if (SessionManager.isLogged(session)) {
            if (!userDao.is_admin(user)){
                throw new AuthorizationException("You are not authorized");
            }
            userDao.deleteUser(user);
            session.invalidate();
        }
        return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
    }

    //find all subscribed


}
