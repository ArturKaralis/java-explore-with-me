package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.ewm.category.dao.CategoryRepository;
import ru.practicum.ewm.category.dto.CategoryDtoResponse;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.exception.IncorrectIdException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

import static ru.practicum.ewm.category.mapper.CategoryMapper.*;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
@Validated
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repo;

    @Transactional
    @Override
    public CategoryDtoResponse save(@NotBlank @Size(max = 50) String name) {
        Category category = new Category();
        category.setName(name);
        return mapToCategoryDtoResponse(repo.saveAndFlush(category));
    }

    @Override
    public List<CategoryDtoResponse> findAll(int from, int size) {
        return mapToCategoryDtoResponse(repo.findAll(PageRequest.of(from / size, size)).getContent());
    }

    @Override
    public CategoryDtoResponse findById(long catId) {
        return mapToCategoryDtoResponse(
                repo.findById(catId).orElseThrow(() -> new IncorrectIdException(catId, "category")));
    }

    @Transactional
    @Override
    public CategoryDtoResponse update(@NotBlank @Size(max = 50) String name, long catId) {
        final Category category = repo.findById(catId).orElseThrow(() -> new IncorrectIdException(catId, "category"));
        category.setName(name);
        return mapToCategoryDtoResponse(category);
    }

    @Transactional
    @Override
    public void delete(long catId) {
        repo.deleteById(catId);
    }
}
