package xyz.iggy.rabbit_arnab.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.iggy.rabbit_arnab.service.RabbitMQProducerService;

@RestController
@RequiredArgsConstructor
public class KeithController {
//    @GetMapping
    public String hiKeith(){
        return "Hi Keith Legolas";
    }

    private final RabbitMQProducerService rabbitMQProducerService;
    @GetMapping
    public void ok() throws JsonProcessingException {
        rabbitMQProducerService.scheduledMsg();
    }
}
