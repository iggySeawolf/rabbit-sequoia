package xyz.iggy.rabbit_arnab.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchJobAPIConsumerService implements CommandLineRunner {
    private static final String API_URL = "https://jobs.sequoiacap.com/api-boards/search-jobs";
    private final String requestBody = """
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
                .body(String.class)
//                .toBodilessEntity()
                .toString();
        log.info("search-jobs api response == \n{}", response);
        return response;
    }


    @Override
    public void run(String... args) throws Exception {
        doPost();
    }
}
