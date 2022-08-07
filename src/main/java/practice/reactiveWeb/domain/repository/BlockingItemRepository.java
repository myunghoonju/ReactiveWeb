package practice.reactiveWeb.domain.repository;

import org.springframework.data.repository.CrudRepository;
import practice.reactiveWeb.domain.entity.Item;

public interface BlockingItemRepository extends CrudRepository<Item, String> {

}
