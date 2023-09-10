package ru.practicum.ewm.event.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
