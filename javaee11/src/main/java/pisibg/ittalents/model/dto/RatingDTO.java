package pisibg.ittalents.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import pisibg.ittalents.model.pojo.Rating;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor

public class RatingDTO {
    long id;
    @NotBlank(message = "Text should not be blank")
    private String rating_text;
    @NotNull(message = "This field should not be null")
    @Range(min = 1, max = 5)
    private int rating_stars;

    public RatingDTO(Rating rating) {
        setId(rating.getId());
        setRating_text(rating.getRating_text());
        setRating_stars(rating.getRating_stars());
    }

}
