package xyz.iggy.rabbit_arnab.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class JobPost {
    //Query: manually put it
    //
    //jobs.title
    //
    //jobs.companyName
    //
    //jobs.skills.label
    //jobs.timestamp
    String queryParameter; // From controller not the json response.
    String companyName;
    String title;
    List<String> skillsTags;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime jobPostedWhen;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime messagePublishedOn;
}