package ru.practicum.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class EndpointHitResponseDto {
    private long id;
    private String app;
    private String uri;
    private String ip;
    private Timestamp timestamp;
}
