package ru.practicum.mainsevice.service;

import ru.practicum.mainsevice.dto.CategoryDto;
import ru.practicum.mainsevice.dto.NewCategoryDto;
import ru.practicum.mainsevice.model.Category;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto categoryDto);

    CategoryDto updateCategory(Integer id, NewCategoryDto categoryDto);

    void deleteCategory(Integer id);

    Category getCategoryById(Integer id);

    List<Category> getAllCategories();
}
