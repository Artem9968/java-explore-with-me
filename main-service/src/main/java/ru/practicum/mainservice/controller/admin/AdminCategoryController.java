package ru.practicum.mainservice.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.dto.category.NewCategoryDto;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.service.category.CategoryService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Validated @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Добавляем новую категорию {}.", newCategoryDto.getName());
        return categoryService.createCategory(newCategoryDto);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@Validated @RequestBody NewCategoryDto newCategoryDto,
                                      @PathVariable int catId) {
        log.info("Редактируем категорию с id={}.", catId);
        return categoryService.updateCategory(catId, newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCategory(@PathVariable int catId) {
        log.info("Категория с id={} удаляется администратором.", catId);
        categoryService.removeById(catId);
    }
}


