package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.mainservice.model.Event;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer>,
        JpaSpecificationExecutor<Event> {

    List<Event> findEventsByOrganizer_Id(int id);

    List<Event> findEventsByIdIn(List<Integer> ids);

    List<Event> findEventsByCategory_Id(int id);
}
