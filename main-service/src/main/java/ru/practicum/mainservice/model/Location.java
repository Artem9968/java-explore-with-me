package ru.practicum.mainservice.model;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Location {

    private Float lat;

    private Float lon;

}
