package ru.practicum.mainservice.model.event;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventConfirmedRequestCount {

    private Integer eventId;

    private Long confirmedRequestCount;
}
