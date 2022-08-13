package practice.reactiveWeb.testcontainers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import practice.reactiveWeb.domain.entity.Item;
import practice.reactiveWeb.domain.repository.ItemRepository;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers // junit5
@ContextConfiguration // context load before run test
@AutoConfigureWebTestClient // set webTestClient
public class RabbitMQ {

    static final String TINY_IMAGE = "rabbitmq:3.7.25-management-alpine";

    @Container
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer(TINY_IMAGE);
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    ItemRepository itemRepository;

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getContainerIpAddress);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
    }

    @Test
    void verifyMessagingThroughAmqp() throws InterruptedException {
        webTestClient.post().uri("/items")
                .bodyValue(new Item("Alf alarm clock", "test clock", 19.9))
                .exchange()
                .expectStatus().isCreated()
                .expectBody();

        // Due to stepVerifier does not exist for rabbitMQ.
        // Neither non-blocking call nor postponed response can not be implemented.
        Thread.sleep(3000L);

        webTestClient.post().uri("/items")
                .bodyValue(new Item("Smurf TV tray", "test tv", 29.99))
                .exchange()
                .expectStatus().isCreated()
                .expectBody();

        Thread.sleep(6000L);

        itemRepository.findAll()
                .as(StepVerifier::create)
                .expectNextMatches(item -> {
                    assertThat(item.getName()).isEqualTo("Alf alarm clock");
                    assertThat(item.getDescription()).isEqualTo("test clock");
                    assertThat(item.getPrice()).isEqualTo(19.9);

                    return true;
                })
                .expectNextMatches(item -> {
                    assertThat(item.getName()).isEqualTo("Smurf TV tray");
                    assertThat(item.getDescription()).isEqualTo("test tv");
                    assertThat(item.getPrice()).isEqualTo(29.99);

                    return true;
                })
                .verifyComplete();
    }
}

