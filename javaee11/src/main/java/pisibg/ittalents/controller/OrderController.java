package pisibg.ittalents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pisibg.ittalents.SessionManager;
import pisibg.ittalents.dao.OrderDAO;
import pisibg.ittalents.exception.AuthorizationException;
import pisibg.ittalents.exception.OrderNotFoundException;
import pisibg.ittalents.model.dto.OrdersByUserDTO;
import pisibg.ittalents.model.pojo.Order;
import pisibg.ittalents.model.pojo.Product;
import pisibg.ittalents.model.pojo.User;
import pisibg.ittalents.model.repository.OrderRepository;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@RestController
public class OrderController extends AbstractController{

    @Autowired
    OrderDAO orderDao;
    @Autowired
    OrderRepository orderRepository;

    @GetMapping(value = "viewOrder/{id}")
    public HashMap<Product, Integer> view(@PathVariable("id") long orderId, HttpSession session) throws SQLException {
        if (!SessionManager.isLogged(session)) {
            throw new AuthorizationException("You have to log in first");
        }
        User user = (User) session.getAttribute("user_logged");
        if (!(orderRepository.existsById(orderId))){
            throw new OrderNotFoundException("Order not found!");
        }
        Order order = orderRepository.getOne(orderId);
        if (!(user.getOrders().contains(order))){
            throw new AuthorizationException("You are not authorized to view this order.");
        }
        else{
        return orderDao.getProductsFromOrder(order);}

    }

    @GetMapping(value = "myorders")
    public List<OrdersByUserDTO> viewAllOrders(HttpSession session) throws SQLException {
        if (!SessionManager.isLogged(session)) {
            throw new AuthorizationException("You have to log in first");
        }
        User user = (User) session.getAttribute("user_logged");

        List <OrdersByUserDTO> orders = orderDao.getAllOrdersByUser(user);
        return orders;
    }

}
