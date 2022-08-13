package practice.reactiveWeb.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import practice.reactiveWeb.domain.entity.Item;
import practice.reactiveWeb.domain.repository.ItemRepository;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmqpItemService {

    private final ItemRepository itemRepository;

    // better than AmqpTemplate.receive(queueName)
    @RabbitListener(
            ackMode = "MANUAL",
            bindings = @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange("reactive-web"),
                    key = "new-items-amqp"))
    public Mono<Void> processNewItemsViaAmqp(Item item) { // consumer method, see AmqpItemController.addNewItemsUsingAmqp
        log.info("Consuming => " + item);
        return itemRepository.save(item).then();
    }
}
