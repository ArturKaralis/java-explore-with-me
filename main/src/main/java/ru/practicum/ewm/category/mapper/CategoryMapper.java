package ru.practicum.ewm.category.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.category.dto.CategoryDtoResponse;
import ru.practicum.ewm.category.model.Category;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CategoryMapper {
    public List<CategoryDtoResponse> mapToCategoryDtoResponse(List<Category> categories) {
        return categories.stream().map(CategoryMapper::mapToCategoryDtoResponse).collect(Collectors.toList());
    }

    public CategoryDtoResponse mapToCategoryDtoResponse(Category category) {
        return CategoryDtoResponse
                .builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
