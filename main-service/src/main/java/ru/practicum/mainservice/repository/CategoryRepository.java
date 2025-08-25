package ru.practicum.mainsevice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainsevice.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
