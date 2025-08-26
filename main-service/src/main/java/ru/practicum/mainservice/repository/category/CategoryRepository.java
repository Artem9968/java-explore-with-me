package ru.practicum.mainservice.repository.category;

import ru.practicum.mainservice.model.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
