package ru.practicum.mainsevice.mapper;

import ru.practicum.mainsevice.dto.CategoryDto;
import ru.practicum.mainsevice.dto.NewCategoryDto;
import ru.practicum.mainsevice.model.Category;

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
