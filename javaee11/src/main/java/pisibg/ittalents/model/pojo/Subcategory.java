package pisibg.ittalents.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="subcategories")
public class Subcategory  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    public Subcategory(long id, String name, Category category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

}
