package practice.reactiveWeb.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import practice.reactiveWeb.domain.service.InventoryService;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final InventoryService inventoryService;

    @GetMapping("/")
    Mono<Rendering> home() {
        Mono<Rendering> result = Mono.just(Rendering
                .view("home.html")
                .modelAttribute("items", inventoryService.getInventory())
                .modelAttribute("cart", inventoryService.getCart("My Cart"))
                .build());

        return result;
    }

    /**
     * - 의문점 -
     * REST 방식에 안맞는 것 같다.
     * post 보내는데 주소 창에 값이 노출되고
     * 그 노출된 값을 받아서 처리 하고 있음
     * get 처리하는데 요청이름만 post??????
     */
    @PostMapping("/add/{id}")
    Mono<String> addToCart(@PathVariable String id) {
        return inventoryService.addItemToCart("My Cart", id).thenReturn("redirect:/");
    }

    @GetMapping("/search")
    Mono<Rendering> search(@RequestParam(required = false) String name,
                           @RequestParam(required = false) String description,
                           @RequestParam boolean useAnd) {
        return Mono.just(Rendering.view("home.html")
                .modelAttribute("items", inventoryService.searchByExample(name, description, useAnd))
                .modelAttribute("cart", inventoryService.searchCart())
                .build());
    }
}
