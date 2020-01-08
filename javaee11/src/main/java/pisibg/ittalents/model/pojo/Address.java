package pisibg.ittalents.model.pojo;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
//TODO table - database
@Table(name = "addresses")

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String city;
    private String address_text;
    private String postal_code;

}
