package practice.reactiveWeb.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.reactiveWeb.domain.entity.Cart;
import practice.reactiveWeb.domain.entity.CartItem;
import practice.reactiveWeb.domain.repository.CartRepository;
import practice.reactiveWeb.domain.repository.ItemRepository;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    public Mono<Cart> addToCart(String cartId, String id) {
        return cartRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .flatMap(cart -> cart.getCartItemList().stream()
                                     .filter(cartItem -> cartItem.getItem().getId().equals(id))
                        .findAny()
                        .map(cartItem -> {
                                            cartItem.increment();
                                            return Mono.just(cart);
                        })
                        .orElseGet(() -> itemRepository.findById(id)
                                                       .map(CartItem::new)
                                                       .doOnNext(cartItem -> cart.getCartItemList().add(cartItem))
                                                       .map(cartItem -> cart)))
                .flatMap(cartRepository::save);
    }
}
