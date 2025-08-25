package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.mainservice.model.EventApprovalStats;
import ru.practicum.mainservice.model.EventRequest;
import java.util.List;

public interface EventRequestRepository extends JpaRepository<EventRequest, Integer> {

    @Query("SELECT new ru.practicum.mainservice.model.EventApprovalStats(r.event.id, COUNT(r)) " +
            "FROM EventRequest r WHERE r.event.id IN (:ids) AND r.requestState = 'APPROVED' " +
            "GROUP BY r.event.id")
    List<EventApprovalStats> getCountApprovedRequests(@Param("ids") List<Integer> ids);

    @Query("SELECT COUNT(r) FROM EventRequest r WHERE r.event.id = :eventId AND r.requestState = 'APPROVED'")
    Integer getCountApprovedRequestsByEventId(@Param("eventId") int eventId);

    List<EventRequest> findAllByEvent_Id(int eventId);

    List<EventRequest> findAllByRequestingUser_Id(int userId);
}