package practice.reactiveWeb.blockHound;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.ReactiveFluentMongoOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import practice.reactiveWeb.domain.entity.Cart;
import practice.reactiveWeb.domain.entity.CartItem;
import practice.reactiveWeb.domain.entity.Item;
import practice.reactiveWeb.domain.repository.CartRepository;
import practice.reactiveWeb.domain.repository.ItemRepository;
import practice.reactiveWeb.domain.service.InventoryService;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class BlockHoundIntegrationTest {

    InventoryService inventoryService;

    @MockBean
    ItemRepository itemRepository;

    @MockBean
    CartRepository cartRepository;

    @MockBean
    private ReactiveFluentMongoOperations fluentMongoOperations;

    @BeforeEach
    void setUp() {
        //given
        Item sampleItem = new Item("item1", "TV tray", "Alf TV tray", 19.99);
        CartItem sampleCartItem = new CartItem(sampleItem);
        Cart sampleCart = new Cart("My Cart", Collections.singletonList(sampleCartItem));

        //when
        when(cartRepository.findById(anyString())).thenReturn(Mono.<Cart> empty().hide()); // find out more about .hide()

        when(itemRepository.findById(anyString())).thenReturn(Mono.just(sampleItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(Mono.just(sampleCart));

        //then
        inventoryService = new InventoryService(itemRepository, fluentMongoOperations, cartRepository);
    }
/*

    @Test
    void blocHoundShouldTrapBlockingCall() {
        Mono.delay(Duration.ofSeconds(1))
                .flatMap(tick -> inventoryService.addItemToCartBlockingCall("My Cart", "item1"))
                .as(StepVerifier::create)
                .verifyErrorSatisfies(throwable -> {
                    Assertions.<Throwable>assertThat(throwable).hasMessageContaining(
                            "block()/blockFirst()/blockLast() are blocking");
                });
    }*/
}
