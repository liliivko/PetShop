package pisibg.ittalents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pisibg.ittalents.SessionManager;
import pisibg.ittalents.exception.AuthorizationException;
import pisibg.ittalents.exception.EmptyCartException;
import pisibg.ittalents.exception.OutOfStockException;
import pisibg.ittalents.exception.ProductNotFoundException;
import pisibg.ittalents.model.dto.ProductFromCartDTO;
import pisibg.ittalents.model.pojo.Order;
import pisibg.ittalents.model.pojo.User;
import pisibg.ittalents.model.repository.OrderRepository;
import pisibg.ittalents.model.repository.ProductRepository;
import pisibg.ittalents.model.pojo.Product;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CartController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;

    @PostMapping(value = "buy/{id}/quantity/{pieces}") // + quantity also from the url? 1 by default?
    public void addToCart(@PathVariable("id") long productId, @PathVariable("pieces") int pieces, HttpSession session) {
        if(!(productRepository.findById(productId).isPresent())){
            throw new ProductNotFoundException("Product not found.");
        }
        Product product = productRepository.findById(productId).get();
        if (!SessionManager.isLogged(session)) {
            throw new AuthorizationException("You have to log in first");
        } else{
          if (session.getAttribute("cart") == null) {
            HashMap<Long, Integer> cart = new HashMap<>();
            cart.put(product.getId(), pieces);
            session.setAttribute("cart", cart);
        } else {
              HashMap<Long, Integer> cart = (HashMap<Long, Integer>) session.getAttribute("cart");
              if (cart.containsKey(product.getId())) {
                  if(!(product.getQuantity()<pieces)) {
                      cart.put(product.getId(), cart.get(product.getId()) + pieces);
                      product.setQuantity(product.getQuantity() - pieces);
                      productRepository.save(product);
                  }
                  else{
                      throw new OutOfStockException("The required quantitiy is not available in stock. Please reduce the amount.");
                  }
              } else {
                  cart.put(product.getId(), pieces);
                  product.setQuantity(product.getQuantity() - pieces);
                  productRepository.save(product);
              }
              session.setAttribute("cart", cart);
          }
        }
    }

    @DeleteMapping(value = "remove/{id}/quantity/{pieces}")
    public void remove(@PathVariable("id") long id, @PathVariable("pieces") int pieces, HttpSession session) throws ProductNotFoundException {
        if(!(productRepository.findById(id).isPresent())){
            throw new ProductNotFoundException("Product not found.");
        }
        Product product = productRepository.findById(id).get();
        if (!SessionManager.isLogged(session)) {
            throw new AuthorizationException("You have to log in first");
        }
        else {
            HashMap<Long, Integer> cart = (HashMap<Long, Integer>) session.getAttribute("cart");
            if (!(cart.containsKey(product.getId()))) {
                throw new ProductNotFoundException("Product not found");
            } else {
                cart.put(product.getId(), cart.get(product.getId()) - pieces);
                if (cart.get(product.getId()) <= 0 ) {
                    cart.remove(product.getId());
                }
            }
            session.setAttribute("cart", cart);
        }
    }

    @GetMapping(value = "myCart")
    public ArrayList<ProductFromCartDTO> viewCart(HttpSession session, HttpServletResponse response) {
        if (!SessionManager.isLogged(session)) {
            throw new AuthorizationException("You have to log in first");
        }
        if (session.getAttribute("cart") == null) {
            return null;
        } else {
            double totalPrice = 0;
            HashMap<Long, Integer> cart = (HashMap<Long, Integer>) session.getAttribute("cart");
            ArrayList<ProductFromCartDTO> cartToview = new ArrayList<>();
            for (Map.Entry e: cart.entrySet()) {
                cartToview.add(new ProductFromCartDTO(productRepository.getOne((Long)e.getKey()), (Integer)e.getValue()));
                totalPrice += productRepository.getOne((Long)e.getKey()).getPrice()*(Integer)e.getValue();
            }
            response.setHeader("total price", String.valueOf(totalPrice));
            return cartToview;
        }
    }

    //TODO - by session invalidation (logout, dies, restart) - DB should be updated with the quantities from all the carts!

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

}
