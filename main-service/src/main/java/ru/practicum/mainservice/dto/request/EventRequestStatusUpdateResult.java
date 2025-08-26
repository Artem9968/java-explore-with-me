package ru.practicum.mainservice.dto.request;

import lombok.ToString;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;

@NoArgsConstructor
@Getter
@ToString
@Setter
public class EventRequestStatusUpdateResult {

   private List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();

  private   List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

}
