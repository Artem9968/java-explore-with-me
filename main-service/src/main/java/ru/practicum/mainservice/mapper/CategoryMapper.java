package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.CategoryDto;
import ru.practicum.mainservice.dto.NewCategoryDto;
import ru.practicum.mainservice.model.Category;

public class CategoryMapper {
    private CategoryMapper() {
    }

    public static Category toCategory(NewCategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }

    public static CategoryDto toDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}
