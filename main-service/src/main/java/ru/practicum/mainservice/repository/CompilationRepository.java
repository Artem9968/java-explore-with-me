package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.model.Compilation;


import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {
    List<Compilation> findAllByPinnedEquals(boolean pinned);
}
