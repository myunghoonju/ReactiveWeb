// not recommended

package practice.reactiveWeb.config;

import org.springframework.boot.CommandLineRunner;
import practice.reactiveWeb.domain.entity.Item;
import practice.reactiveWeb.domain.repository.BlockingItemRepository;

//@Component
public class RepositoryDataLoader {

    //@Bean
    CommandLineRunner initialize(BlockingItemRepository repository) {
        return args -> {
            repository.save(new Item("alarm clock", 19.99));
            repository.save(new Item("tv tray", 24.99));
        };
    }
}
