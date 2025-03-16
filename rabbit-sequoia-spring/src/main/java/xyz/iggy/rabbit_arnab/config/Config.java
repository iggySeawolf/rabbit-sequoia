package xyz.iggy.rabbit_arnab.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.text.SimpleDateFormat;

@Configuration
public class Config {
    @Bean
    public RestClient restClient(RestTemplateBuilder builder) {
        return RestClient.create();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    public SimpleDateFormat simpleDateFormat(){
        return new SimpleDateFormat("Z");
    }
}
