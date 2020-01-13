package pisibg.ittalents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pisibg.ittalents.SessionManager;
import pisibg.ittalents.dao.DiscountDAO;
import pisibg.ittalents.dao.UserDAO;
import pisibg.ittalents.exception.AuthorizationException;
import pisibg.ittalents.exception.NotFoundException;
import pisibg.ittalents.model.dto.DiscountDTO;
import pisibg.ittalents.model.pojo.Discount;
import pisibg.ittalents.model.pojo.User;
import pisibg.ittalents.model.repository.AddressRepository;
import pisibg.ittalents.model.repository.DiscountRepository;
import pisibg.ittalents.model.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
public class AdminPanel extends AbstractController {
    @Autowired
    private DiscountDAO discountDAO;
    @Autowired
    private UserDAO userDao;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    DiscountRepository discountRepository;

    @GetMapping(value = "/users/all")
    public List<User> getAllUsers(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You have to log in");
        }
        if (user.is_admin()) {
            return userRepository.findAll();
        } else {
            throw new AuthorizationException("You are not authorized");
        }
    }

    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundException("User not found");
        }
    }

    //TODO fix
    @DeleteMapping("/users/delete")
    public ResponseEntity<String> deleteUser(HttpSession session, Long id) {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You need to log in first");
        }
        if (SessionManager.isLogged(session)) {
            if (!findUserById(user.getId()).is_admin()) { //TODO check
                throw new AuthorizationException("You are not authorized");
            }
            userRepository.deleteById(id);
            session.invalidate();
        }
        return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
    }

    //TODO find all subscribed, add/ delete product
    //TODO validation for start and end date and DTO or Pojo
    @PostMapping("/discounts/add")
    public ResponseEntity<String> addDiscount(@RequestBody DiscountDTO discountDTO,
                                              HttpSession session) {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You need to log in first");
        }

        if (SessionManager.isLogged(session)) {
            if (!findUserById(user.getId()).is_admin()) { //TODO check
                throw new AuthorizationException("You are not authorized");
            }

            Discount discount = new Discount();
            discount.setName(discountDTO.getName());
            discount.setAmount(discountDTO.getAmount());
            discount.setDate_from(discountDTO.getDate_from());
            discount.setDate_to(discountDTO.getDate_to());
            discountRepository.save(discount);
        }
        return new ResponseEntity<>("Discount added", HttpStatus.CREATED);
    }

    //TODO delete discount
    //todo CHECK LONG, return DTO
    @PostMapping("applydiscount")
    public ResponseEntity<String> applytoSubcategory(@RequestParam("discount_id") long discountId,
                                                     @RequestParam("sucategory_id") long subcategoryId,
                                                     HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You need to log in first");
        }

        if (SessionManager.isLogged(session)) {
            if (!findUserById(user.getId()).is_admin()) { //TODO check
                throw new AuthorizationException("You are not authorized");
            }
            // get discount id and add it to products which are from certain category
            discountDAO.applyDiscount(discountId,subcategoryId);
        }
        return new ResponseEntity<>("Discount added", HttpStatus.CREATED);
    }
}
