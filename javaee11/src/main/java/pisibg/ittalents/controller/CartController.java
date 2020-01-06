package pisibg.ittalents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.ittalents.exception.ProductNotFoundException;
import pisibg.ittalents.repository.ProductRepository;
import pisibg.ittalents.model.Product;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RestController
public class CartController {
    @Autowired
    ProductRepository productRepository;

    @PostMapping(value = "buy/{id}")
    public void addToCart(@PathVariable("id") long id, HttpSession session) {
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
                cart.put(productRepository.findById(id).get(), cart.get(productRepository.findById(id).get())+1);
            }
            session.setAttribute("cart", cart);
        }
    }

    @GetMapping(value = "remove/{id}")
    public void remove(@PathVariable("id") long id, HttpSession session) throws ProductNotFoundException {
        HashMap<Product, Integer> cart = (HashMap<Product, Integer>) session.getAttribute("cart");
        int index = this.exists(id, cart);
        if (index == -1) {
            throw new ProductNotFoundException("Product not found");
        } else {
            cart.put(productRepository.findById(id).get(), cart.get(productRepository.findById(id).get())-1);
            if(cart.get(productRepository.findById(id).get())==0){
                cart.remove(productRepository.findById(id).get());
            }
        }
        session.setAttribute("cart", cart);
    }

    private int exists(long id, HashMap<Product, Integer> cart) {

        if(cart.containsKey(productRepository.findById(id).get())){
                return 1;}
                else{
                    return -1;
                }
    }


    //getPriceAtm - helper, is not a RequestMapping
    //viewCart - GetMapping
    //buyProductsFromCart - PostMapping
}
