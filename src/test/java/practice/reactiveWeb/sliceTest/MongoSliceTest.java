package practice.reactiveWeb.sliceTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import practice.reactiveWeb.domain.entity.Item;
import practice.reactiveWeb.domain.repository.ItemRepository;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class MongoSliceTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    void itemRepositorySavesItems() {
        Item sampleItem = new Item("name", "desc", 1.99);
        itemRepository.save(sampleItem)
                .as(StepVerifier::create)
                .expectNextMatches(item -> {
                    assertThat(item.getId()).isNotNull();
                    assertThat(item.getName()).isEqualTo("name");
                    assertThat(item.getDescription()).isEqualTo("desc");
                    assertThat(item.getPrice()).isEqualTo(1.99);

                    return true;
                })
                .verifyComplete();
    }
}
