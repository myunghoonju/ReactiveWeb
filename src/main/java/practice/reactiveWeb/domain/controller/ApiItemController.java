package practice.reactiveWeb.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import practice.reactiveWeb.domain.entity.Item;
import practice.reactiveWeb.domain.repository.ItemRepository;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class ApiItemController {

    private final ItemRepository itemRepository;

    @GetMapping("/api/items/{id}")
    Mono<Item> findOne(@PathVariable String id) {
        return itemRepository.findById(id);
    }

    @PostMapping("/api/items")
    Mono<ResponseEntity<?>> addNewItem(@RequestBody Mono<Item> item) {
        return item.flatMap(newItem -> itemRepository.save(newItem))
                .map(savedItem -> ResponseEntity
                        .created(URI.create("/api/items/" + savedItem.getId()))
                        .body(savedItem));
    }

    @PutMapping("/api/items/{id}")
    Mono<ResponseEntity<?>> updateItem(@RequestBody Mono<Item> oldItem,
                                       @PathVariable String id) {
        return oldItem.map(content ->
                        new Item(id, content.getName(), content.getDescription(), content.getPrice()))
                .flatMap(itemRepository::save)
                .map(ResponseEntity::ok);
    }
}
