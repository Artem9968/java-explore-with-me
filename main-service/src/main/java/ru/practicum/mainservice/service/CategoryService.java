package ru.practicum.mainservice.service;

import ru.practicum.mainservice.dto.CategoryDto;
import ru.practicum.mainservice.dto.NewCategoryDto;
import ru.practicum.mainservice.model.Category;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto categoryDto);

    CategoryDto updateCategory(Integer id, NewCategoryDto categoryDto);

    void deleteCategory(Integer id);

    Category getCategoryById(Integer id);

    List<Category> getAllCategories();
}
