package xyz.iggy.rabbit_arnab;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RabbitArnabApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitArnabApplication.class, args);
	}

}
