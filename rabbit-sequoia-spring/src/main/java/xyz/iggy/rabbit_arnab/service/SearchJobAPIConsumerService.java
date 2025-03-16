package xyz.iggy.rabbit_arnab.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

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
    private final ObjectMapper objectMapper;


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


    public void deserializeJson() throws JsonProcessingException {

        String doPostPost = doPost();
        JsonNode rootNode = objectMapper.readTree(doPostPost);
        JsonNode jobsArray = rootNode.path("jobs");
        List<String> allSkills = new ArrayList<>();
        if (jobsArray.isArray()) {

            for (JsonNode job : jobsArray) {
                JsonNode companyName = job.path("companyName");
                JsonNode title = job.path("title");
                JsonNode skillTags = job.path("skills");
                JsonNode jobPostedWhen = job.path("timeStamp");

                List<String> skillsPerJob = new ArrayList<>();
                if(skillTags.isArray()){
                    for(JsonNode skills: skillTags){
                        skillsPerJob.add(skills.path("label").asText());
                    }
                }

                System.out.println(companyName +" "+ title +" "+skillsPerJob+ " "+jobPostedWhen);

            }
        }
    }

    @Override
    public void run(String... args) throws Exception {
//        try {
//            Writer writer = new FileWriter("./rabbit-sequoia-spring/spring-boot-jobs.json");
//            BufferedWriter bw = new BufferedWriter(writer);
//            bw.write(doPost());
//            bw.close();
//            log.info("api response written to file");
//        } catch (IOException e) {
//            log.error("Could not write response to file.");
//            throw new RuntimeException(e);
//        }
        deserializeJson();
    }
}
