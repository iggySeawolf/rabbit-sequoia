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
import xyz.iggy.rabbit_arnab.model.JobPost;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchJobAPIConsumerService implements CommandLineRunner {
    private static final String API_URL = "https://jobs.sequoiacap.com/api-boards/search-jobs";
    private static final String queryParam = """
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


    public JsonNode doPost(String requestBody){
        JsonNode response = restClient.post()
                .uri(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(JsonNode.class);
        return response;
    }


    public void deserializeJson() throws JsonProcessingException {
        JsonNode apiResponse = doPost(queryParam);
        List<JobPost> listSendToRabbit = new ArrayList<>();

        JsonNode jobsArray = apiResponse.path("jobs");
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
                JobPost jobPost = JobPost.builder()
                        .companyName(companyName.asText())
                        .title(title.asText())
                        .skillsTags(skillsPerJob)
//                        .jobPostedWhen(jobPostedWhen)
                        .build();
                log.info("jobpost::{}",jobPost);
                String jopPostAsString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jobPost);
                log.info("jobpost__JSON::\n{}",jopPostAsString);
            }
        }
    }

    @Override
    public void run(String... args) throws Exception {
        deserializeJson();
    }
}
