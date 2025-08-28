package ru.practicum.mainservice.storage.request;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.mainservice.model.event.EventConfirmedRequestCount;
import ru.practicum.mainservice.model.request.ParticipationRequest;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Integer> {

    List<ParticipationRequest> findAllByRequester_Id(int userId);

    List<ParticipationRequest> findAllByEvent_Id(int eventId);

    @Query("SELECT COUNT(r) FROM ParticipationRequest r WHERE r.event.id = :eventId AND r.status = 'CONFIRMED'")
    Integer countConfirmedByEventId(@Param("eventId") int eventId);

    @Query("SELECT new ru.practicum.mainservice.model.event.EventConfirmedRequestCount(r.event.id, COUNT(r)) " +
            "FROM ParticipationRequest r WHERE r.event.id IN (:ids) AND r.status = 'CONFIRMED' " +
            "GROUP BY r.event.id")
    List<EventConfirmedRequestCount> countConfirmedByEventIdIn(@Param("ids") List<Integer> ids);

}
