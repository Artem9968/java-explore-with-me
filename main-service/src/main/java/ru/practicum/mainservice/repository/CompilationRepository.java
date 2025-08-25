package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.model.EventCollection;

import java.util.List;

public interface CompilationRepository extends JpaRepository<EventCollection, Integer> {
    List<EventCollection> findAllByPinnedEquals(boolean pinned);
}
