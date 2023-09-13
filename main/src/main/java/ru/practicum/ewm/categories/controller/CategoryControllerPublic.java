package ru.practicum.ewm.categories.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryControllerPublic {

    CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Получаем запрос на получение Категорий: from={}, size={}", from, size);
        List<CategoryDto> categoryDtoList = categoryService.getCategories(from, size);
        log.info("Возвращаем {} элемет(а/ов) Категорий", categoryDtoList.size());
        return categoryDtoList;
    }

    @GetMapping("/{categoryId}")
    public CategoryDto getCategoryById(@PathVariable Long categoryId) {
        log.info("Получаем запрос на получение Категории: categoryId={}", categoryId);
        CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
        log.info("Возвращаем CategoryDto={}", categoryDto);
        return categoryDto;
    }
}
