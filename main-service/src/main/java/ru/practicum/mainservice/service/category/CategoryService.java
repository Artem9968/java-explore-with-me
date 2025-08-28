package ru.practicum.mainservice.service.category;

import java.util.List;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.category.NewCategoryDto;
import ru.practicum.mainservice.model.category.Category;

public interface CategoryService {

    List<Category> findAll();

    Category findById(Integer id);

    void removeById(Integer id);

    CategoryDto updateCategory(Integer id, NewCategoryDto categoryDto);

    CategoryDto createCategory(NewCategoryDto categoryDto);

}
