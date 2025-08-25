package ru.practicum.mainservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventApprovalStats {

    private Integer eventId;

    private Long approvedParticipantsCount;
}
