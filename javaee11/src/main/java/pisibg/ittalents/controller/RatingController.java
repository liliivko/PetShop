package pisibg.ittalents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pisibg.ittalents.SessionManager;
import pisibg.ittalents.exception.AuthorizationException;
import pisibg.ittalents.exception.BadRequestException;
import pisibg.ittalents.exception.NotFoundException;
import pisibg.ittalents.exception.ProductNotFoundException;
import pisibg.ittalents.model.dto.RatingDTO;
import pisibg.ittalents.model.pojo.Product;
import pisibg.ittalents.model.pojo.Rating;
import pisibg.ittalents.model.pojo.User;
import pisibg.ittalents.model.repository.ProductRepository;
import pisibg.ittalents.model.repository.RatingRepository;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
public class RatingController extends AbstractController {

    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    ProductRepository productRepository;

    //TODO make it only accessible by admin
    @GetMapping("/ratings")
    public List<Rating> getAll() {
        return ratingRepository.findAll();
    }

    @GetMapping("/users/ratings")
    public List<Rating> getRatings(HttpSession session) {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You need to log in first");
        }
        return ratingRepository.findAllByUser_Id(user.getId());
    }

    @PostMapping("ratings/{id}")//TODO if there isn't a product
    public ResponseEntity<Rating> rateProduct(@RequestBody RatingDTO ratingDto,
                                              @PathVariable("id") Long id, HttpSession session) {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You need to log in first");
        }
        Rating rating = new Rating();
        rating.setUser(user);
        rating.setProduct(getProductById(id));
        rating.setRating_text(ratingDto.getRating_text());
        rating.setRating_stars(ratingDto.getRating_stars());
        ratingRepository.save(rating);
        return new ResponseEntity<>(rating, HttpStatus.CREATED);
    }

    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new NotFoundException("Product not found");
        }
    }

    public Rating getRatingById(Long id) {
        Optional<Rating> rating = ratingRepository.findById(id);
        if (rating.isPresent()) {
            return rating.get();
        } else {
            throw new BadRequestException("Rating not found");
        }
    }

    @GetMapping("/products/{id}/ratings/")
    public List<Rating> getRatingsForProduct(@PathVariable("id") Long id, HttpSession session) {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You need to log in first");
        }
        return ratingRepository.findAllByProduct_Id(id);
    }

    @DeleteMapping("/ratings/{id}")
    public void deleteById(@PathVariable("id") Long id, HttpSession session) {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You need to log in first");
        }
        if (user.getId() != getRatingById(id).getUser().getId()) {
            throw new AuthorizationException("You are not authorized to delete this rating");
        }
        ratingRepository.deleteById(id);
    }
}
