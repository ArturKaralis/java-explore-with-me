package ru.practicum.ewm.main.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.main.event.model.Event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Compilation {

    private Long id;
    private boolean pinned;
    private String title;
    private Set<Event> events = new HashSet<>();

    public Map<String, Object> mapToDb() {
        Map<String, Object> compilationFields = new HashMap<>();

        compilationFields.put("pinned", pinned);
        compilationFields.put("title", title);

        return compilationFields;
    }
}
