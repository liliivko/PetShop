package pisibg.ittalents.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pisibg.ittalents.dao.OrderDAO;
import pisibg.ittalents.exception.AuthorizationException;
import pisibg.ittalents.exception.NotFoundException;
import pisibg.ittalents.exception.OrderNotFoundException;
import pisibg.ittalents.model.dto.OrdersByUserDTO;
import pisibg.ittalents.model.dto.ProductFromCartDTO;
import pisibg.ittalents.model.dto.ProductWithOrderPriceDTO;
import pisibg.ittalents.model.pojo.Order;
import pisibg.ittalents.model.pojo.Product;
import pisibg.ittalents.model.pojo.User;
import pisibg.ittalents.model.repository.OrderRepository;
import pisibg.ittalents.model.repository.PaymentMethodRepository;
import pisibg.ittalents.model.repository.ProductRepository;
import pisibg.ittalents.utils.SessionManager;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class OrderController extends AbstractController{

    @Autowired
    OrderDAO orderDao;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @GetMapping(value = "viewOrder/{id}")
    public List<ProductWithOrderPriceDTO> view(@PathVariable("id") long orderId, HttpSession session, HttpServletResponse response) throws SQLException {
        if (!SessionManager.isLogged(session)) {
            throw new AuthorizationException("You have to log in first");
        }
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (!(orderRepository.existsById(orderId))){
            throw new OrderNotFoundException("Order not found!");
        }
        Order order = orderRepository.getOne(orderId);
        if (!(user.getOrders().contains(order))){
            throw new AuthorizationException("You are not authorized to view this order.");
        }
        else {
            List <ProductFromCartDTO> pfc= orderDao.getProductsFromOrder(order);
            List<ProductWithOrderPriceDTO> products = new ArrayList<>();
            double totalPrice = 0;
            for (ProductFromCartDTO p:pfc) {
                Optional<Product> product = productRepository.findById(p.getId());
                if(product.isPresent()){
                    totalPrice += p.getPrice()*p.getQuantity();
                    Product pr = product.get();
                    pr.setQuantity(p.getQuantity());
                    products.add(new ProductWithOrderPriceDTO(pr, order));
                }
                else{
                    throw new NotFoundException("Product not found");
                }
            }
            response.setHeader("total price", String.valueOf(totalPrice));
            return products;
        }
    }

    @GetMapping(value = "myorders")
    public List<OrdersByUserDTO> viewAllOrders(HttpSession session) throws SQLException {
        if (!SessionManager.isLogged(session)) {
            throw new AuthorizationException("You have to log in first");
        }
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        List <OrdersByUserDTO> orders = orderDao.getAllOrdersByUser(user);
        return orders;
    }

}
