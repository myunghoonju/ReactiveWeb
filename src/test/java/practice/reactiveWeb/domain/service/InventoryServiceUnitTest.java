package practice.reactiveWeb.domain.service;

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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
class InventoryServiceUnitTest {

    InventoryService inventoryService; // class under test(a.k.a., CUT)

    @MockBean // fake bean, provided to app
    private ItemRepository itemRepository;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private ReactiveFluentMongoOperations fluentMongoOperations;

    /**
     *
     *  below code is same as above @MockBean lines
     *  @MockBean implies the object is not for test
     *
        @BeforeEach
        void setUp() {
            ItemRepository itemRepo = mock(ItemRepository.class);
            CartRepository cartRepo = mock(CartRepository.class);
        }
     */

    @BeforeEach
    void setUp() {
        //given
        Item sampleItem = new Item("item1", "TV tray", "Alf TV tray", 19.99);
        CartItem sampleCartItem = new CartItem(sampleItem);
        Cart sampleCart = new Cart("My Cart", Collections.singletonList(sampleCartItem));

        //when
        when(cartRepository.findById(anyString())).thenReturn(Mono.empty());
        when(itemRepository.findById(anyString())).thenReturn(Mono.just(sampleItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(Mono.just(sampleCart));

        //then
        inventoryService = new InventoryService(itemRepository, fluentMongoOperations, cartRepository);
    }

    @Test
    void addItemToEmptyCartShouldProduceOneCartItem() {
        inventoryService.addItemToCart("My Cart", "item1")
                .as(StepVerifier::create)
                .expectNextMatches(cart -> {
                    assertThat(cart.getCartItemList())
                            .extracting(CartItem::getQuantity)
                            .containsExactly(1);

                    assertThat(cart.getCartItemList())
                            .extracting(cartItem -> cartItem.getItem().getId())
                            .containsExactly("item1");

                    return true;
                })
                .verifyComplete();
    }
}