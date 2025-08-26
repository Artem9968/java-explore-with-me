package ru.practicum.mainservice.service.category;

import ru.practicum.mainservice.storage.event.EventRepository;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.model.event.Event;
import ru.practicum.mainservice.storage.category.CategoryRepository;
import ru.practicum.mainservice.mapper.category.CategoryMapper;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.dto.category.NewCategoryDto;
import ru.practicum.mainservice.model.category.Category;
import ru.practicum.mainservice.dto.category.CategoryDto;
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
            throw new ConflictException("Невозможно удалить категорию");
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
