package pisibg.ittalents.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }
//    @OneToMany(
//            mappedBy = "categoryId",
//            cascade = CascadeType.ALL
//    )
//    private List<Subcategory> subcategoriesFromTheCategory;

//    public void addSubcategory(Subcategory subcategory) {
//        subcategoriesFromTheCategory.add(subcategory);
//        subcategory.setCategory(this);
//    }
//
//    public void removeSubcategory(Subcategory subcategory) {
//        subcategoriesFromTheCategory.remove(subcategory);
//    }
//
//
//
}
