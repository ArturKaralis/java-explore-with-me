package ru.practicum.ewm.stats.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.stats.dto.HitDto;
import ru.practicum.ewm.stats.dto.StatsDto;
import ru.practicum.ewm.stats.server.mapper.HitMapper;
import ru.practicum.ewm.stats.server.model.Hit;
import ru.practicum.ewm.stats.server.service.StatsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatsController.class)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class StatsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StatsService statsService;

    static ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    @BeforeAll
    static void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getStats() throws Exception {
        String app = "ewm-main-service";
        String uri = "/events/1";
        Long hits = 6L;

        LocalDateTime start = LocalDateTime.of(2020, 5, 5, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2035, 5, 5, 0, 0, 0);
        List<String> uris = new ArrayList<>();
        uris.add(uri);
        Boolean unique = true;
        StatsDto statsDto = new StatsDto(app, uri, hits);
        StatsDto statsDto1 = new StatsDto("main-service", "/events/2", 3L);
        List<StatsDto> statsDtoList = new ArrayList<>();
        statsDtoList.add(statsDto);
        statsDtoList.add(statsDto1);

        when(statsService.getStats(start, end, uris, unique)).thenReturn(statsDtoList);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        mockMvc.perform(get("/stats")
                        .param("start", start.format(formatter))
                        .param("end", end.format(formatter))
                        .param("uris", String.join(",", uris))
                        .param("unique", String.valueOf(unique)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].app").value("ewm-main-service"))
                .andExpect(jsonPath("$[0].uri").value("/events/1"))
                .andExpect(jsonPath("$[0].hits").value("6"))
                .andExpect(jsonPath("$[1].app").value("main-service"))
                .andExpect(jsonPath("$[1].uri").value("/events/2"))
                .andExpect(jsonPath("$[1].hits").value("3"));
    }

    @Test
    void addHit() throws Exception {
        Long id = 1L;
        String app = "ewm-main-service";
        String uri = "/events/1";
        String ip = "192.163.0.1";
        LocalDateTime timestamp = LocalDateTime.of(2022, 9, 6, 11, 0, 23);

        Hit hit = new Hit(id, app, uri, ip, timestamp);
        HitDto hitDto = HitMapper.toHitDto(hit);
        HitDto newHitDto = new HitDto();
        newHitDto.setId(id);
        newHitDto.setApp(app);
        newHitDto.setUri(uri);
        newHitDto.setIp(ip);
        newHitDto.setTimestamp(timestamp);

        when(statsService.addHit(hitDto)).thenReturn(newHitDto);

        mockMvc.perform(post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(newHitDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.app").value("ewm-main-service"))
                .andExpect(jsonPath("$.uri").value("/events/1"))
                .andExpect(jsonPath("$.ip").value("192.163.0.1"));
    }
}