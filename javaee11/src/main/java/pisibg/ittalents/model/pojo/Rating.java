package pisibg.ittalents.model.pojo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pisibg.ittalents.model.dto.HiddenPasswordUserDTO;
import pisibg.ittalents.model.dto.RegisterUserDTO;
import pisibg.ittalents.model.repository.RatingRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;
    private String rating_text;
    private int rating_stars;


    public Rating(Rating dto) {
        setRating_text(dto.rating_text);
        setRating_stars(dto.rating_stars);
    }
}

