package pisibg.ittalents.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pisibg.ittalents.exception.AuthorizationException;
import pisibg.ittalents.exception.BadRequestException;
import pisibg.ittalents.exception.NotFoundException;
import pisibg.ittalents.model.dto.RatingDTO;
import pisibg.ittalents.model.pojo.Product;
import pisibg.ittalents.model.pojo.Rating;
import pisibg.ittalents.model.pojo.User;
import pisibg.ittalents.model.repository.ProductRepository;
import pisibg.ittalents.model.repository.RatingRepository;
import pisibg.ittalents.utils.SessionManager;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class RatingController extends AbstractController {

    @Autowired
   private RatingRepository ratingRepository;
    @Autowired
    ProductRepository productRepository;


    @GetMapping("/users/ratings")
    public List<RatingDTO> getRatings(HttpSession session) {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You need to log in first");
        }
        List<Rating> ratings = ratingRepository.findAllByUser_Id(user.getId());
        List<RatingDTO> ratingDTOList = new ArrayList<>();
        if (!ratings.isEmpty()) {
            for (Rating rating : ratings) {
                RatingDTO ratingDTO = new RatingDTO(rating);
                ratingDTOList.add(ratingDTO);
            }
            return ratingDTOList;
        }
        throw new NotFoundException("Nothing to show");
    }

    @PostMapping("ratings/{id}")
    public ResponseEntity<RatingDTO> rateProduct(@RequestBody RatingDTO ratingDto,
                                                @PathVariable("id") long id, HttpSession session) {
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
        return new ResponseEntity<>(new RatingDTO(rating), HttpStatus.CREATED);
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
    public List<Rating> getRatingsForProduct(@PathVariable("id") long id, HttpSession session) {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You need to log in first");
        }
        return ratingRepository.findAllByProduct_Id(id);
    }

    @DeleteMapping("/ratings/{id}")
    public ResponseEntity<String> deleteMyRating(@PathVariable("id") long id, HttpSession session) {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You need to log in first");
        }
        if (user.getId() != getRatingById(id).getUser().getId()) {
            throw new AuthorizationException("You are not authorized to delete this rating");
        }
        ratingRepository.deleteById(id);
        return new ResponseEntity<>("Rating deleted", HttpStatus.CREATED);
    }
}
