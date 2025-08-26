package ru.practicum.mainservice.service;

import java.util.List;
import ru.practicum.mainservice.dto.CategoryDto;
import ru.practicum.mainservice.dto.NewCategoryDto;
import ru.practicum.mainservice.model.Category;

public interface CategoryService {

    List<Category> findAll();

    Category findById(Integer id);

    void removeById(Integer id);

    CategoryDto updateCategory(Integer id, NewCategoryDto categoryDto);

    CategoryDto createCategory(NewCategoryDto categoryDto);

}
