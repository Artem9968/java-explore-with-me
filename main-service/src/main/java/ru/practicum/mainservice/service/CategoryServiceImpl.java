package ru.practicum.mainservice.service;

import ru.practicum.mainservice.repository.EventRepository;
import ru.practicum.mainservice.exception.DataConflictException;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.repository.CategoryRepository;
import ru.practicum.mainservice.mapper.CategoryMapper;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.dto.NewCategoryDto;
import ru.practicum.mainservice.model.Category;
import ru.practicum.mainservice.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    private Category findCategoryOrThrow(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
    }

    private void checkCategoryHasEvents(Integer categoryId) {
        List<Event> events = eventRepository.findByCategoryId(categoryId);
        if (!events.isEmpty()) {
            throw new DataConflictException("Невозможно удалить категорию");
        }
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Integer id) {
        return findCategoryOrThrow(id);
    }

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto categoryDto) {
        Category category = CategoryMapper.toCategory(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional
    public void removeById(Integer id) {
        Category category = findCategoryOrThrow(id);
        checkCategoryHasEvents(id);
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Integer id, NewCategoryDto categoryDto) {
        Category category = findCategoryOrThrow(id);
        category.setName(categoryDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        return CategoryMapper.toDto(updatedCategory);
    }
}
