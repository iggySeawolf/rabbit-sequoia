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
    String queryParameter;
    List<String> skillsTags;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime messagePublishedOn;
}