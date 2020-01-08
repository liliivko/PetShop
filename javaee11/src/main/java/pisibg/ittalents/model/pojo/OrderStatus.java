package pisibg.ittalents.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class OrderStatus {

    private long id;
    private String statusName;

    //TODO associate id with name, form DB?
    //TODO get and set status? Admin?

    public OrderStatus(long id){
        this.id = id;
    }
}
