package xyz.iggy.rabbit_arnab.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import xyz.iggy.rabbit_arnab.model.JobPost;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchJobAPIConsumerService implements CommandLineRunner {
    private static final String API_URL = "https://jobs.sequoiacap.com/api-boards/search-jobs";
    private/* static final */String queryParam = """
            {
                "meta": {
                     "size": 10
                 },
                 "board": {
                     "id": "sequoia-capital",
                     "isParent": true
                 }
            }
            """;
    //Get
//    private static final String JSON_FILE;
    private final ObjectMapper objectMapper;

    private final RestClient restClient;

    String asdasd() throws JsonProcessingException {
//        JsonNode jsonNode = objectMapper.readTree(queryParam);
//        jsonNode.

        //Feels illegal </3
        return queryParam += """
                    ,
                     "query": {
                         "skills": [
                             "resume:Spring Boot"
                         ]
                     }
                }
                """;
    }


    private String try2(String queryParam) throws JsonProcessingException {
        JsonNode baseQuery = objectMapper.readTree(this.queryParam);
        ObjectNode finalQuery = (ObjectNode) baseQuery;
        ArrayNode skillsJSON = objectMapper.createArrayNode();
        skillsJSON.add(queryParam);
        finalQuery.set("query",skillsJSON);
        return finalQuery.toString();
    }


    private JsonNode doPost(String requestBody){
        JsonNode response = restClient.post()
                .uri(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(JsonNode.class);
        return response;
    }


    public List<JobPost> deserializeJson(String queryParam) throws JsonProcessingException {
        JsonNode apiResponse = doPost(try2(queryParam));
        List<JobPost> listSendToRabbit = new ArrayList<>();

        JsonNode jobsArray = apiResponse.path("jobs");
        if (jobsArray.isArray()) {
            for (JsonNode job : jobsArray) {
                JsonNode companyName = job.path("companyName");
                JsonNode title = job.path("title");
                JsonNode skillTags = job.path("skills");
                JsonNode prefSkillsTags = job.path("preferredSkills");
                JsonNode reqSkillsTags = job.path("requiredSkills");
                JsonNode jobPostedWhen = job.path("timeStamp");
                Set<String> skillsPerJob = new HashSet<>();
                if(skillTags.isArray()){
                    for(JsonNode skills: skillTags){
                        skillsPerJob.add(skills.path("label").asText());
                    }
                }
                if(prefSkillsTags.isArray()){
                    Stream<JsonNode> stream = StreamSupport.stream(prefSkillsTags.spliterator(), true);
                    skillsPerJob.addAll(stream
                            .map(it -> it.path("label").asText()) // Extract "label" field
                            .collect(Collectors.toList()));
                }
                else{
                    log.error("error pref skills");
                }
                if(reqSkillsTags.isArray()){
                    Stream<JsonNode> stream = StreamSupport.stream(reqSkillsTags.spliterator(), true);
                    skillsPerJob.addAll(stream
                            .map(it -> it.path("label").asText()) // Extract "label" field
                            .collect(Collectors.toList()));
                }
                else{
                    log.error("error req skills");
                }
                JobPost jobPost = JobPost.builder()
                        .queryParameter(queryParam)
                        .companyName(companyName.asText())
                        .title(title.asText())
                        .skillsTags(skillsPerJob)
                        .jobPostedWhen(Date.from(Instant.parse(jobPostedWhen.asText())))
                        .messagePublishedOn(Date.from(Instant.now()))
                        .build();

                listSendToRabbit.add(jobPost);
            }
        }
        return listSendToRabbit;
    }

    @Override
    public void run(String... args) throws Exception {
//        deserializeJson("resume:Spring Boot");
    }
}
