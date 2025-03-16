package xyz.iggy.rabbit_arnab.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@ToString
public class JobPost {
    //Query: manually put it
    //
    //jobs.title
    //
    //jobs.companyName
    //
    //jobs.skills.label
    //jobs.timestamp
    private String queryParameter; // From controller not the json response.
    private String companyName;
    private String title;
    private List<String> skillsTags;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime jobPostedWhen;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime messagePublishedOn;
}