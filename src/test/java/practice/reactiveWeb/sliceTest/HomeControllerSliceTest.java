package practice.reactiveWeb.sliceTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import practice.reactiveWeb.domain.controller.HomeController;
import practice.reactiveWeb.domain.entity.Cart;
import practice.reactiveWeb.domain.entity.Item;
import practice.reactiveWeb.domain.service.InventoryService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebFluxTest(HomeController.class)
public class HomeControllerSliceTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    InventoryService inventoryService;

    @Test
    void homePage() {
        when(inventoryService.getInventory())
                .thenReturn(
                        Flux.just(
                                    new Item("id1", "name1", "desc1", 1.99),
                                    new Item("id2", "name2", "desc2", 9.99)
                                 ));

        when(inventoryService.getCart("My Cart")).thenReturn(Mono.just(new Cart("My Cart")));

        client.get().uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .consumeWith(exchangeResult -> {
                    assertThat(exchangeResult.getResponseBody()).contains("<form method=\"post\"");
                });
    }
}
