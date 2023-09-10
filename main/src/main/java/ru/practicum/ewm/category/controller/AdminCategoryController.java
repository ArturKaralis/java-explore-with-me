package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDtoResponse;
import ru.practicum.ewm.category.service.CategoryService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDtoResponse save(@RequestBody Map<String, String> body) {
        return service.save(body.get("name"));
    }

    @PatchMapping("/{catId}")
    public CategoryDtoResponse update(@RequestBody Map<String, String> body,
                                      @PathVariable long catId) {
        return service.update(body.get("name"), catId);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long catId) {
        service.delete(catId);
    }
}
