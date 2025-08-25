package ru.practicum.mainsevice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainsevice.dto.CategoryDto;
import ru.practicum.mainsevice.dto.NewCategoryDto;
import ru.practicum.mainsevice.exception.DataConflictException;
import ru.practicum.mainsevice.exception.NotFoundException;
import ru.practicum.mainsevice.mapper.CategoryMapper;
import ru.practicum.mainsevice.model.Category;
import ru.practicum.mainsevice.model.Event;
import ru.practicum.mainsevice.repository.CategoryRepository;
import ru.practicum.mainsevice.repository.EventRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto categoryDto) {
        Category savedCategory = categoryRepository.save(CategoryMapper.toCategory(categoryDto));
        return CategoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Integer id, NewCategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не найдена категория id=" + id));
        category.setName(categoryDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        return CategoryMapper.toDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Integer id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не найдена категория id=" + id));
        List<Event> events = eventRepository.findEventsByCategory_Id(id);
        if (events.size() > 0) {
            throw new DataConflictException(
                    "Категория id=" + id + " не пустая.");
        }
        categoryRepository.deleteById(id);
    }

    @Override

    public Category getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не найдена категория id=" + id));
        return category;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
