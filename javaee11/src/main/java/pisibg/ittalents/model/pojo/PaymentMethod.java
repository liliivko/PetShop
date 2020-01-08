package pisibg.ittalents.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentMethod {

    private long id;
    private String type;

    //could have 'private String details', should be also in the DB?
}
