package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.App;

import java.util.Optional;

public interface AppStorage extends JpaRepository<App, Long> {
    Optional<App> findByName(String name);
}
