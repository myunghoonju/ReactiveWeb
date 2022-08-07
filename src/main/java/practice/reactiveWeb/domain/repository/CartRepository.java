package practice.reactiveWeb.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import practice.reactiveWeb.domain.entity.Cart;

public interface CartRepository extends ReactiveCrudRepository<Cart, String> {

}
