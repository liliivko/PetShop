package pisibg.ittalents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pisibg.ittalents.SessionManager;
import pisibg.ittalents.dao.RatingDao;
import pisibg.ittalents.exception.AuthorizationException;
import pisibg.ittalents.model.dto.HiddenPasswordUserDTO;
import pisibg.ittalents.model.dto.RegisterUserDTO;
import pisibg.ittalents.model.pojo.Rating;
import pisibg.ittalents.model.pojo.User;
import pisibg.ittalents.model.repository.RatingRepository;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class RatingController {

    @Autowired
    RatingDao ratingDao;
    @Autowired
    RatingRepository ratingRepository;

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

    @PostMapping("ratings/{id}")
    public ResponseEntity<String> register(@RequestBody HttpSession session) {
        User user = (User) session.getAttribute(SessionManager.USER__LOGGED);
        if (user == null) {
            throw new AuthorizationException("You need to log in first");
        }
        Rating rating= new Rating();
        rating.setUser(user);
        ratingRepository.save(rating);
        return new ResponseEntity<>( HttpStatus.OK);
    }


}
