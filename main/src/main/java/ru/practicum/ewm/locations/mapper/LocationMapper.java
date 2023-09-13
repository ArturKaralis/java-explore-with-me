package ru.practicum.ewm.locations.mapper;

import ru.practicum.ewm.locations.dto.LocationDto;
import ru.practicum.ewm.locations.model.Location;

public class LocationMapper {
    public static Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }

    public static LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}
