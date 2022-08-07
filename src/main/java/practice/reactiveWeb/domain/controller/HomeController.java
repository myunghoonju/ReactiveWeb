package practice.reactiveWeb.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import practice.reactiveWeb.domain.entity.Cart;
import practice.reactiveWeb.domain.repository.CartRepository;
import practice.reactiveWeb.domain.repository.ItemRepository;
import practice.reactiveWeb.domain.service.CartService;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CartService cartService;
    //FIXME no service code!
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;


    //FIXME no service code!
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

   /**
    *  - 의문점 -
    * REST 방식에 안맞는 것 같다.
    * post 보내는데 주소 창에 값이 노출되고
    * 그 노출된 값을 받아서 처리 하고 있음
    * get 처리하는데 요청이름만 post??????
    * */
    @PostMapping("/add/{id}")
    Mono<String> addToCart(@PathVariable String id) {
        return cartService.addToCart("My Cart", id)
                          .thenReturn("redirect:/");
    }
}
