package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.mainservice.model.EventApprovalStats;
import ru.practicum.mainservice.model.EventRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<EventRequest, Integer> {
    List<EventRequest> findAllByRequester_Id(int userId);

    List<EventRequest> findAllByEvent_Id(int eventId);

    @Query("SELECT COUNT(r) FROM Request r WHERE r.event.id = :eventId AND r.status = 'CONFIRMED'")
    Integer getCountConfirmedRequestsByEventId(@Param("eventId") int eventId);

    @Query("SELECT new ru.practicum.mainservice.model.EventConfirmedRequestCount(r.event.id, COUNT(r)) " +
            "FROM Request r WHERE r.event.id IN (:ids) AND r.status = 'CONFIRMED' " +
            "GROUP BY r.event.id")
    List<EventApprovalStats> getCountConfirmedRequests(@Param("ids") List<Integer> ids);
}
