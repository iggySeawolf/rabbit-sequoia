package xyz.iggy.rabbit_arnab.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import xyz.iggy.rabbit_arnab.model.JobPost;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitMQProducerService implements CommandLineRunner {


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
    private final SearchJobAPIConsumerService apiConsumerService;
    private final List<String> queryParams = List.of("resume:.NET"/*, "resume:Spring Boot"*/);
    private final ObjectMapper objectMapper;

    public void sendMessage(String msg){
        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg);
    }

//    @Scheduled(cron="*/2 * * * * *")
    public void scheduledMsg() throws JsonProcessingException {
        List<JobPost> jobPosts = new ArrayList<>();
        for (String param : queryParams) {
            List<JobPost> jobPostListByQueryParam = apiConsumerService.deserializeJson(param);
            jobPosts.addAll(jobPostListByQueryParam);
        }
        for(JobPost jp: jobPosts){
            String rabbitMessage = objectMapper.writeValueAsString(jp);
            sendMessage(rabbitMessage);
            log.info("Sending msg to Rabbit, {}", rabbitMessage);
        }

    }

    @Override
    public void run(String... args) throws Exception {
        scheduledMsg();
    }
}
