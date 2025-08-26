package ru.practicum.mainservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.mainservice.model.Event;

public interface EventRepository extends JpaRepository<Event, Integer>,
        JpaSpecificationExecutor<Event> {

    List<Event> findByInitiatorId(int id);

    List<Event> findByIdIn(List<Integer> ids);

    List<Event> findByCategoryId(int id);
}
