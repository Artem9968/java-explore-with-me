package ru.practicum.mainservice.mapper.category;

import ru.practicum.mainservice.model.category.Category;
import ru.practicum.mainservice.dto.category.NewCategoryDto;
import ru.practicum.mainservice.dto.category.CategoryDto;

public class CategoryMapper {

    public static CategoryDto toDto(Category category) {
        CategoryDto result = new CategoryDto();
        result.setId(category.getId());
        result.setName(category.getName());
        return result;
    }

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        Category result = new Category();
        result.setName(newCategoryDto.getName());
        return result;
    }
}

