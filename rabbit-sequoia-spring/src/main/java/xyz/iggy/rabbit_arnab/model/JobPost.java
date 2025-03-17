package xyz.iggy.rabbit_arnab.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    private Set<String> skillsTags;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private Date jobPostedWhen;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private Date messagePublishedOn;
}