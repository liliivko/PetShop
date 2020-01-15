package pisibg.ittalents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
import utils.SessionManager;

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
    private AddressRepository addressRepository;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private NotificationService notificationService;


    @GetMapping(value = "/users/all")
    public List<User> getAllUsers(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You have to log in");
        }
        if (user.isAdmin()) {
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
            if (!findUserById(user.getId()).isAdmin()) { //TODO check
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
            if (!findUserById(user.getId()).isAdmin()) { //TODO check
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
    @PostMapping("applyDiscount")
    public ResponseEntity<String> applytoSubcategory(@RequestParam("discount_id") long discountId,
                                                     @RequestParam("subcategory_id") long subcategoryId,
                                                     HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You need to log in first");
        }

        if (SessionManager.isLogged(session)) {
            if (!findUserById(user.getId()).isAdmin()) {
                throw new AuthorizationException("You are not authorized");
            }
            Discount discount = getDiscountById(discountId);
            if (discount != null) {
                discountDAO.applyDiscount(discountId, subcategoryId);
                informAllSubscribers(discount.getName());
            }
        }
        return new ResponseEntity<>("Discount added", HttpStatus.CREATED);
    }


    public Discount getDiscountById(long discountId) {
        Optional<Discount> discount = discountRepository.findById(discountId);
        if (discount.isPresent()) {
            return discount.get();
        } else {
            throw new NotFoundException("The resource you are trying to reach is not found");

        }
    }

        //@GetMapping(value = "/inform/")
        public void informAllSubscribers (String name){
            List<User> subscribers = userRepository.findAllBySubscribedTrue();
            for (User user : subscribers) {
                Runnable runnable = () -> {
                    notificationService.sendMail(user.getEmail(), name);
                };
                new Thread(runnable).start();
            }
        }


    }
