package practice.reactiveWeb.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.reactiveWeb.domain.entity.Dish;
import practice.reactiveWeb.domain.service.KitchenService;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class ServerController {

    private final KitchenService kitchen;

    @GetMapping(value = "/server", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Dish> serveDishes() {
        return kitchen.getDishes();
    }

    @GetMapping(value = "/served-dishes", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Dish> deliverDishes() {
        return kitchen.getDishes().map(Dish::deliver);
    }
}
