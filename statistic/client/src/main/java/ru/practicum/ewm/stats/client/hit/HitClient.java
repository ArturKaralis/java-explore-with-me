package ru.practicum.ewm.stats.client.hit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.stats.client.client.BaseClient;
import ru.practicum.ewm.stats.dto.HitDto;
import ru.practicum.ewm.stats.dto.StatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@PropertySource(value = "classpath:stats-application.properties")
public class HitClient extends BaseClient {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public HitClient(@Value("${stats-service.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public List<StatsDto> getStats(String start, String end, List<String> uris, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        ResponseEntity<Object> objectResponseEntity = get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
        return objectMapper.convertValue(objectResponseEntity.getBody(), new TypeReference<>() {
        });
    }

    public ResponseEntity<Object> addHit(HttpServletRequest request) {
        HitDto hitDto = HitDto.builder()
                .app((String) request.getAttribute("app_name"))
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        return post("/hit", hitDto);
    }
}
