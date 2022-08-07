package practice.reactiveWeb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import practice.reactiveWeb.domain.entity.Item;

@Component
public class TemplateDatabaseLoader {

    @Bean
    CommandLineRunner initialize(MongoOperations mongo) {
        return args -> {
            mongo.save(new Item("alarm clock", 19.99));
            mongo.save(new Item("tv tray", 24.99));
        };
    }
}
