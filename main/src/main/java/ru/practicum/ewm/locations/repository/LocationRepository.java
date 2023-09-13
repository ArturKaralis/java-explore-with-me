package ru.practicum.ewm.locations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.locations.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

    boolean existsByLatAndLon(Double lat, Double lon);

    Location findByLatAndLon(Double lat, Double lon);
}
