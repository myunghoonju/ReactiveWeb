package practice.reactiveWeb.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.mongodb.core.ReactiveFluentMongoOperations;
import org.springframework.stereotype.Service;
import practice.reactiveWeb.domain.entity.Cart;
import practice.reactiveWeb.domain.entity.CartItem;
import practice.reactiveWeb.domain.entity.Item;
import practice.reactiveWeb.domain.repository.CartRepository;
import practice.reactiveWeb.domain.repository.ItemRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    private final CartRepository cartRepository;

    public Flux<Item> getInventory() {
        return itemRepository.findAll();
    }

    public Mono<Cart> getCart(String cartId) {
        return cartRepository.findById(cartId).defaultIfEmpty(new Cart("My Cart"));
    }

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

    public Mono<Cart> searchCart() {
        return cartRepository.findById("My Cart").defaultIfEmpty(new Cart("My Cart"));
    }

    public Mono<Cart> addItemToCart(String cartId, String itemId) {
        return cartRepository.findById(cartId)
                .log("foundCart")
                .defaultIfEmpty(new Cart(cartId))
                .log("emptyCart")
                .flatMap(cart -> cart.getCartItemList().stream()
                        .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                        .findAny()
                        .map(cartItem -> {
                            cartItem.increment();
                            return Mono.just(cart).log("new Cart Item");
                        })
                        .orElseGet(() -> {
                            return itemRepository.findById(itemId)
                                    .log("fetchedItem")
                                    .map(item -> new CartItem(item))
                                    .log("cart item")
                                    .map(cartItem -> {
                                        cart.getCartItemList().add(cartItem);
                                        return cart;
                                    }).log("added Cart Item");
                        }))
                .log("cart with another item")
                .flatMap(cart -> cartRepository.save(cart))
                .log("saved cart");
    }
}
