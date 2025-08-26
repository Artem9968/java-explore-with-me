package ru.practicum.mainservice.storage.compilation;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.model.compilation.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {
    List<Compilation> findByPinned(boolean pinned);
}
