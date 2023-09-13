package ru.practicum.ewm.categories.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.service.CategoryService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryControllerAdmin {

    CategoryService categoryService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("Получаем запрос на создание категории: categoryDto={}", categoryDto);
        CategoryDto newCategoryDto = categoryService.addCategory(categoryDto);
        log.info("Возвращаем созданную категорию: {}", newCategoryDto);
        return newCategoryDto;
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId,
                                      @RequestBody @Valid CategoryDto categoryDto) {
        log.info("Получаем запрос на обновление: categoryId={}, categoryDto={}", catId, categoryDto);
        CategoryDto newCategoryDto = categoryService.updateCategory(catId, categoryDto);
        log.info("Возвращаем обновленную категорию: {}", newCategoryDto);
        return newCategoryDto;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Получаем запрос на удаление категории: categoryId={}", catId);
        categoryService.deleteCategory(catId);
        log.info("Категория categoryId={} удалена.", catId);
    }
}
