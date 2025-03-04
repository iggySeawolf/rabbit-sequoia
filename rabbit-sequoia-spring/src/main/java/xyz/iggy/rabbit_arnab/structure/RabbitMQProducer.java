package xyz.iggy.rabbit_arnab.structure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class RabbitMQProducer {


    /*
    *
    * https://youtu.be/wsgJU5S1rRY?t=7610
    * Timestamp of good real world usecase of WebFlux.
    *
    * */

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;
//    """"
    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String msg){
        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg);
    }

    @Scheduled(cron="*/2 * * * * *")
    public void scheduledMsg() throws JsonProcessingException {
        JobPostingEntity jobPostingEntity = JobPostingEntity.builder()
                .queryParameter(".NET")
                .skillsTags(List.of(".NET",
                        "SQl",
                        "Linux",
                        "React"))
                .messagePublishedOn(LocalDateTime.now())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        String jsonString = objectMapper.writeValueAsString(jobPostingEntity);
//        String msg = "SpringBoot created @ " + Instant.now();
//        String msg = jobPostingEntity.toString();

        log.info("Sending msg to Rabbit, {}", jsonString);
        sendMessage(jsonString);
    }

}
