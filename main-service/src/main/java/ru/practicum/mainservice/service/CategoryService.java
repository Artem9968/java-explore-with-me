package ru.practicum.mainservice.service;

import ru.practicum.mainservice.dto.CategoryResponse;
import ru.practicum.mainservice.dto.CategoryCreateRequest;
import ru.practicum.mainservice.model.Category;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryCreateRequest categoryDto);

    CategoryResponse updateCategory(Integer id, CategoryCreateRequest categoryDto);

    void deleteCategory(Integer id);

    Category getCategoryById(Integer id);

    List<Category> getAllCategories();
}
