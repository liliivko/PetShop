package pisibg.ittalents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.ittalents.exception.*;
import pisibg.ittalents.model.dto.ProductFromCartDTO;
import pisibg.ittalents.model.dto.ProductWithCurrentPriceDTO;
import pisibg.ittalents.model.pojo.Address;
import pisibg.ittalents.model.pojo.Order;
import pisibg.ittalents.model.pojo.User;
import pisibg.ittalents.model.repository.AddressRepository;
import pisibg.ittalents.model.repository.OrderRepository;
import pisibg.ittalents.model.repository.PaymentMethodRepository;
import pisibg.ittalents.model.repository.ProductRepository;
import pisibg.ittalents.model.pojo.Product;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CartController extends AbstractController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    PaymentMethodRepository paymentMethodRepository;
    @Autowired
    AddressRepository addressRepository;

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
                int quantity = cart.get(product.getId());
                if(quantity>pieces){
                    cart.put(product.getId(), cart.get(product.getId()) - pieces);
                    product.setQuantity(product.getQuantity() + pieces);
                    productRepository.save(product);
                } else{
                    cart.remove(product.getId());
                    product.setQuantity(product.getQuantity() + quantity);
                    productRepository.save(product);
                }

            }
            session.setAttribute("cart", cart);
        }
    }

    @GetMapping(value = "myCart")
    public ArrayList<ProductWithCurrentPriceDTO> viewCart(HttpSession session, HttpServletResponse response) throws SQLException {
        if (!SessionManager.isLogged(session)) {
            throw new AuthorizationException("You have to log in first");
        }
        if (session.getAttribute("cart") == null) {
            return null;
        } else {
            double totalPrice = 0;
            HashMap<Long, Integer> cart = (HashMap<Long, Integer>) session.getAttribute("cart");
            ArrayList<ProductWithCurrentPriceDTO> cartToview = new ArrayList<>();
            for (Map.Entry e: cart.entrySet()) {
                ProductWithCurrentPriceDTO productInCart = new ProductWithCurrentPriceDTO(productRepository.getOne((Long)e.getKey()), (Integer)e.getValue());
                cartToview.add(productInCart);
                totalPrice += productInCart.getPrice()*productInCart.getQuantity();
            }
            response.setHeader("total price", String.valueOf(totalPrice));
            return cartToview;
        }
    }

    //TODO - by session invalidation (logout, dies, restart) - DB should be updated with the quantities from all the carts!

    @PostMapping(value = "checkout/paymentmethod/{paymentmethod}/address/{address}")
    public void checkOut(HttpSession session, @PathVariable("paymentmethod") long paymentMethod,
                         @PathVariable("address") long address) {
        if (!SessionManager.isLogged(session)) {
            throw new AuthorizationException("You have to log in first");
        }
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (session.getAttribute("cart") == null) {
            throw new EmptyCartException("Cart is empty, nothing to order.");}
        if(!(paymentMethodRepository.existsById(paymentMethod))){
            throw new InvalidPaymentMethodException("The payment method is invalid.");}
        if(!(addressRepository.existsById(address))){
            throw new NotFoundException("Address not found. Please add address to your profile.");}
        Address ordersAddress = addressRepository.getOne(address);
        if (!(user.getAddresses().contains(ordersAddress))){
            throw new AuthorizationException("You are not authorized to view this order.");
        } else {
            HashMap<Long, Integer> cart = (HashMap<Long, Integer>) session.getAttribute("cart");
            HashMap<Product, Integer> cartToOrder = new HashMap<>();
            for (Map.Entry e: cart.entrySet()) {
                cartToOrder.put(productRepository.getOne((Long)e.getKey()), (Integer)e.getValue());
            }
            Order order = new Order(user, cartToOrder, paymentMethod, ordersAddress);
            for (Map.Entry e: cartToOrder.entrySet()) {
                order.addProduct((Product)e.getKey());
            }
            orderRepository.save(order);
            user.getOrders().add(order);
            cart.clear();
        }
    }

}
