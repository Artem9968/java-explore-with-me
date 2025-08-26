package ru.practicum.mainservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.mainservice.model.EventConfirmedRequestCount;
import ru.practicum.mainservice.model.Request;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findAllByRequester_Id(int userId);

    List<Request> findAllByEvent_Id(int eventId);

    @Query("SELECT COUNT(r) FROM Request r WHERE r.event.id = :eventId AND r.status = 'CONFIRMED'")
    Integer countConfirmedByEventId(@Param("eventId") int eventId);

    @Query("SELECT new ru.practicum.mainservice.model.EventConfirmedRequestCount(r.event.id, COUNT(r)) " +
            "FROM Request r WHERE r.event.id IN (:ids) AND r.status = 'CONFIRMED' " +
            "GROUP BY r.event.id")
    List<EventConfirmedRequestCount> countConfirmedByEventIdIn(@Param("ids") List<Integer> ids);
}
