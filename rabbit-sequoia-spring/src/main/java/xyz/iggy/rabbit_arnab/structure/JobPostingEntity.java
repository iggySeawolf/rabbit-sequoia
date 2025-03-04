package xyz.iggy.rabbit_arnab.structure;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class JobPostingEntity {
    String queryParameter;
    List<String> skillsTags;


    //metadata
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime messagePublishedOn;

}