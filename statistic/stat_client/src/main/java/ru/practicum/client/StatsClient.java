package ru.practicum.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.EndpointHitDto;
import dto.ViewStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StatsClient extends BaseClient {

    @Value("${app-name}")
    private static String APP_NAME;

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public void createHit(HttpServletRequest httpServletRequest) {
        post(new EndpointHitDto(
                APP_NAME,
                httpServletRequest.getRequestURI(),
                httpServletRequest.getRemoteAddr(),
                LocalDateTime.now()));
    }

    public void createHit(HttpServletRequest httpServletRequest, String uri) {
        EndpointHitDto endpointHitDto = new EndpointHitDto(
                APP_NAME,
                uri,
                httpServletRequest.getRemoteAddr(),
                LocalDateTime.now());

        post(endpointHitDto);
    }

    public Map<Long, Long> getHits(List<Long> eventIds) {
        return getHits(eventIds, false);
    }

    public Map<Long, Long> getHits(List<Long> eventIds, boolean unique) {
        if (eventIds != null && eventIds.size() == 0)
            return new HashMap<>();
        StringBuilder sbUrl = new StringBuilder();
        String start = "1100-01-01 00:00:00";
        String end = "2100-01-01 00:00:00";

        Iterator<Long> id = eventIds.iterator();
        sbUrl.append("/events/")
                .append(id.next());
        while (id.hasNext()) {
            sbUrl.append("/events/").append(id.next());
        }

        ResponseEntity<Object> objects = get("stats?start={start}&end={end}&uris={uris}&unique={unique}", Map.of(
                "start", start,
                "end", end,
                "uris", sbUrl.toString(),
                "unique", unique
        ));
        ObjectMapper objectMapper = new ObjectMapper();
        List<ViewStats> viewStats = objectMapper.convertValue(objects.getBody(), new TypeReference<>() {
        });
        if (viewStats == null) {
            return new HashMap<>();
        } else {
            return viewStats.stream()
                    .collect(Collectors.toMap(
                            vs -> Long.parseLong(vs.getUri().split("/", 0)[2]),
                            ViewStats::getHits));
        }
    }
}

