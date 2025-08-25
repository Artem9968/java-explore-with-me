package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.CategoryResponse;
import ru.practicum.mainservice.dto.CategoryCreateRequest;
import ru.practicum.mainservice.model.Category;

public class CategoryMapper {
    private CategoryMapper() {
    }

    public static Category toCategory(CategoryCreateRequest dto) {
        Category category = new Category();
        category.setName(dto.getCategoryName());
        return category;
    }

    public static CategoryResponse toDto(Category category) {
        CategoryResponse dto = new CategoryResponse();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}
