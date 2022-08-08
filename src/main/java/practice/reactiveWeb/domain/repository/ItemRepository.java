package practice.reactiveWeb.domain.repository;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import practice.reactiveWeb.domain.entity.Item;

public interface ItemRepository extends ReactiveCrudRepository<Item, String>,
                                        ReactiveQueryByExampleExecutor<Item> {

}
