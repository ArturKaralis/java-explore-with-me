package ru.practicum.converter;

import lombok.experimental.UtilityClass;
import dto.EndpointHitDto;
import ru.practicum.model.App;
import ru.practicum.model.EndpointHit;

@UtilityClass
public class EndpointHitConverter {

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto, App app) {
        return new EndpointHit(null, app, endpointHitDto.getUri(),
                endpointHitDto.getIp(), endpointHitDto.getTimestamp());
    }
}
