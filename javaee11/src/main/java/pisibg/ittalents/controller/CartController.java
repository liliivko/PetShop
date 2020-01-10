package pisibg.ittalents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.ittalents.exception.EmptyCartException;
import pisibg.ittalents.exception.ProductNotFoundException;
import pisibg.ittalents.model.pojo.Order;
import pisibg.ittalents.model.pojo.User;
import pisibg.ittalents.model.repository.OrderRepository;
import pisibg.ittalents.model.repository.ProductRepository;
import pisibg.ittalents.model.pojo.Product;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RestController
public class CartController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;

    @PostMapping(value = "buy/{id}") // + quantity also from the url? 1 by default?
    public void addToCart(@PathVariable("id") long id, HttpSession session) {
        //TODO Logged User validation + getting user id!
        if (session.getAttribute("cart") == null) {
            HashMap<Product, Integer> cart = new HashMap<>();
            cart.put(productRepository.findById(id).get(), 1);
            session.setAttribute("cart", cart);
        } else {
            HashMap<Product, Integer> cart = (HashMap<Product, Integer>) session.getAttribute("cart");
            int index = this.exists(id, cart);
            if (index == -1) {
                cart.put(productRepository.findById(id).get(), 1);
            } else {
                cart.put(productRepository.findById(id).get(), cart.get(productRepository.findById(id).get()) + 1);
            }
            session.setAttribute("cart", cart);
        }
    }

    @DeleteMapping(value = "remove/{id}")
    public void remove(@PathVariable("id") long id, HttpSession session) throws ProductNotFoundException {
        //TODO Logged User validation
        HashMap<Product, Integer> cart = (HashMap<Product, Integer>) session.getAttribute("cart");
        int index = this.exists(id, cart);
        if (index == -1) {
            throw new ProductNotFoundException("Product not found");
        } else {
            cart.put(productRepository.findById(id).get(), cart.get(productRepository.findById(id).get()) - 1);
            if (cart.get(productRepository.findById(id).get()) == 0) {
                cart.remove(productRepository.findById(id).get());
            }
        }
        session.setAttribute("cart", cart);
    }

    @GetMapping(value = "myCart")
    public HashMap<Product, Integer> viewCart(HttpSession session) {
        //TODO Logged user validation
        if (session.getAttribute("cart") == null) {
            //TODO Cart is empty - Exception or plain text?
            return null;
        } else {
            return (HashMap<Product, Integer> ) session.getAttribute("cart");

            //TODO Total Price should be returned as well
        }
    }

    //TODO send products from Cart to Order! and in DB
    @PostMapping(value = "checkout")
    public void checkOut(HttpSession session) {
        //TODO logged user validation
        if (session.getAttribute("cart") == null) {
            throw new EmptyCartException("Cart is empty, nothing to order.");
        } else {
            //is that supposed to be here??! or in OrderController - static method in controller?
            Order order = new Order((User) session.getAttribute("user"), (HashMap<Product, Integer>) session.getAttribute("cart"));
            orderRepository.save(order);
        }
    }

    private int exists(long id, HashMap<Product, Integer> cart) {
        if (cart.containsKey(productRepository.findById(id).get())) {
            return 1;
        } else {
            return -1;
        }
    }

    //TODO getPriceAtm - helper, is not a RequestMapping?
}
