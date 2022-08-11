package practice.reactiveWeb.apidoc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import practice.reactiveWeb.domain.controller.ApiItemController;
import practice.reactiveWeb.domain.entity.Item;
import practice.reactiveWeb.domain.repository.ItemRepository;
import practice.reactiveWeb.domain.service.InventoryService;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@AutoConfigureRestDocs
@WebFluxTest(controllers = ApiItemController.class)
public class ApiItemControllerDocTest {

    @Autowired
    WebTestClient webTestClient;
    @MockBean
    InventoryService inventoryService;
    @MockBean
    ItemRepository itemRepository;

    @Test
    void findAllItem() {
        when(itemRepository.findAll()).thenReturn(
                Flux.just(new Item("item1", "alf alarm clock", "figgin' clock", 19.99)));

        webTestClient.get().uri("/api/items").exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("findAll", preprocessResponse(prettyPrint())));
    }
}
