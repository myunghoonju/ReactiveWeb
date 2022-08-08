package practice.reactiveWeb.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.mongodb.core.ReactiveFluentMongoOperations;
import org.springframework.stereotype.Service;
import practice.reactiveWeb.domain.entity.Item;
import practice.reactiveWeb.domain.repository.ItemRepository;
import reactor.core.publisher.Flux;

import static org.springframework.data.domain.ExampleMatcher.matchingAll;
import static org.springframework.data.domain.ExampleMatcher.matchingAny;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ItemRepository itemRepository;
    private final ReactiveFluentMongoOperations fluentMongoOperations;

    public Flux<Item> searchByExample(String name, String description, boolean useAnd) {
        Item item = new Item(name, description, 0.0);
        ExampleMatcher matcher = (useAnd ? matchingAll() : matchingAny())
                                    .withStringMatcher(StringMatcher.CONTAINING)
                                    .withIgnoreCase()
                                    .withIgnorePaths("price");

        Example<Item> probe = Example.of(item, matcher);

        return itemRepository.findAll(probe);
    }

    public Flux<Item> searchByFluentExample(String name, String description, boolean useAnd) {
        Item item = new Item(name, description, 0.0);
        ExampleMatcher matcher = (useAnd ? matchingAll() : matchingAny())
                                    .withStringMatcher(StringMatcher.CONTAINING)
                                    .withIgnoreCase()
                                    .withIgnorePaths("price");

        return fluentMongoOperations.query(Item.class)
                                    .matching(query(byExample(Example.of(item, matcher)))).all();
    }

    public Flux<Item> searchByFluentExample(String name, String description) {
        return fluentMongoOperations.query(Item.class)
                                    .matching(query(where("tv tray").is(name).and("Smurf").is(description))).all();
    }
}
