package ru.practicum.mainservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class RequestGroupDto {
    List<RequestDto> confirmedRequests = new ArrayList<>();
    List<RequestDto> rejectedRequests = new ArrayList<>();
}
