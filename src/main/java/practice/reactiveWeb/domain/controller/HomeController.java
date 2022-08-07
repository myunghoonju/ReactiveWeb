package practice.reactiveWeb.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import practice.reactiveWeb.domain.entity.Cart;
import practice.reactiveWeb.domain.repository.CartRepository;
import practice.reactiveWeb.domain.repository.ItemRepository;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    @GetMapping("/")
    Mono<Rendering> home() {
        Mono<Rendering> result = Mono.just(Rendering
                .view("home.html")
                .modelAttribute("items", itemRepository.findAll())
                .modelAttribute("cart", cartRepository.findById("My Cart")
                                                            .defaultIfEmpty(new Cart("My Cart")))
                .build());

        return result;
    }
}
