package xyz.iggy.rabbit_arnab.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchJobAPIConsumerService implements CommandLineRunner {
    private static final String API_URL = "https://jobs.sequoiacap.com/api-boards/search-jobs";
    private static final String requestBody = """
            {
                "meta": {
                     "size": 10
                 },
                 "board": {
                     "id": "sequoia-capital",
                     "isParent": true
                 },
                 "query": {
                     "skills": [
                         "resume:Spring Boot"
                     ]
                 }
            }
            """;

    private final RestClient restClient;

    public String doPost(){
        String response = restClient.post()
                .uri(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(String.class);
//        log.info("search-jobs api response == \n{}", response);
        return response;
    }


    @Override
    public void run(String... args) throws Exception {
        try {
            Writer writer = new FileWriter("./rabbit-sequoia-spring/spring-boot-jobs.json");
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(doPost());
            bw.close();
            log.info("api response written to file");
        } catch (IOException e) {
            log.error("Could not write response to file.");
            throw new RuntimeException(e);
        }
    }
}
