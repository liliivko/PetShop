package pisibg.ittalents.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class RatingDto {
    private String rating_text;
    private int rating_stars;
}
