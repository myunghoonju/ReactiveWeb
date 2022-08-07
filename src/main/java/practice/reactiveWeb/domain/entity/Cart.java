package practice.reactiveWeb.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    private String id;

    private List<CartItem> cartItemList;

    public Cart(String id) {
        this.id = id;
    }
}
