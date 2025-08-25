package ru.practicum.mainservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventCollectionResponse {

    private Boolean isPinned;

    private String collectionTitle;

    private List<EventShortDto> events = new ArrayList<>();

    private int id;
}
