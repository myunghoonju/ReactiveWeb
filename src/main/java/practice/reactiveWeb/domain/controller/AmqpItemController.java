package practice.reactiveWeb.domain.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import practice.reactiveWeb.domain.entity.Item;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AmqpItemController {

    private final AmqpTemplate template;
    // subscribeOn:: 동일 스레드, 선언 위치 상관없음.
    // publishOn:: 새로 지정한 스레드, 선언 위치 상관있음.
    // Schedulers.boundedElastic():: 작업량에 따라 스레드 숫자가 늘어나거나 줄어드는 스레드풀
    @PostMapping("/items")
    Mono<ResponseEntity<?>> addNewItemsUsingAmqp(@RequestBody Mono<Item> item) { // publisher method see AmqpItemService.processNewItemsViaAmqp
        log.info("Publishing starts with {}", item);
        return item.subscribeOn(Schedulers.boundedElastic()) // for blocking api
                .flatMap(content -> Mono.fromCallable(() -> {
                    log.info("reactive-web, new-items-amqp content:: {}", content.toString());
                    template.convertAndSend("reactive-web", "new-items-amqp", content);
                    return ResponseEntity.created(URI.create("/items")).build();
                }));
    }
}
