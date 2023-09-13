package ru.practicum.ewm.categories.mapper;

import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.model.Category;

public class CategoryMapper {
    public static Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}