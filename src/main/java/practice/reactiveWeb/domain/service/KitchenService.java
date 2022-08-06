package practice.reactiveWeb.domain.service;

import org.springframework.stereotype.Service;
import practice.reactiveWeb.domain.entity.Dish;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class KitchenService {

    public Flux<Dish> getDishes() {
        return Flux.<Dish>generate(
                sink -> sink.next(randomDish())
        ).delayElements(Duration.ofMillis(250));
    }

    private Dish randomDish() {
        return menu.get(picker.nextInt(menu.size()));
    }

    private List<Dish> menu = Arrays.asList(
            new Dish("chicken"),
            new Dish("noodle"),
            new Dish("beef"));


    private Random picker = new Random();
}
