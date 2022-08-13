// not recommended

/*
package practice.reactiveWeb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import practice.reactiveWeb.domain.entity.Item;
import practice.reactiveWeb.domain.repository.BlockingItemRepository;

@Component
public class RepositoryDataLoader {

    @Bean
    CommandLineRunner initialize(BlockingItemRepository repository) {
        return args -> {
            repository.save(new Item("alarm clock", 19.99));
            repository.save(new Item("tv tray", 24.99));
        };
    }
}
*/
