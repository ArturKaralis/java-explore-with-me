package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDtoResponse;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public interface CategoryService {
    CategoryDtoResponse save(@NotBlank @Size(max = 50) String name);

    List<CategoryDtoResponse> findAll(int from, int size);

    CategoryDtoResponse findById(long catId);

    CategoryDtoResponse update(@NotBlank @Size(max = 50) String name, long catId);

    void delete(long catId);
}
