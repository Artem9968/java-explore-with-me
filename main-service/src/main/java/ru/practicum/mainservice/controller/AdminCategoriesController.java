package ru.practicum.mainservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.dto.CategoryResponse;
import ru.practicum.mainservice.dto.CategoryCreateRequest;
import ru.practicum.mainservice.service.CategoryService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/categories")
public class AdminCategoriesController {
    private final CategoryService categoryService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(@Validated @RequestBody CategoryCreateRequest categoryDto) {
        log.info("Создаем категорию {}.", categoryDto.getCategoryName());
        return categoryService.createCategory(categoryDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse updateCategory(@Validated @RequestBody CategoryCreateRequest categoryDto,
                                           @PathVariable int id) {
        log.info("Обновляем категорию id={}.", id);
        return categoryService.updateCategory(id, categoryDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int id) {
        log.info("Администратор удаляет категорию id={}.", id);
        categoryService.deleteCategory(id);
    }
}
