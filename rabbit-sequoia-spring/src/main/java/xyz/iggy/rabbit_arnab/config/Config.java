package xyz.iggy.rabbit_arnab.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class Config {
    @Bean
    public RestClient restClient(RestTemplateBuilder builder) {
        return RestClient.create();
    }
}
