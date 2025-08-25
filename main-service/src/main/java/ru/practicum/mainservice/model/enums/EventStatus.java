package ru.practicum.mainservice.model.enums;

import java.util.Optional;

public enum EventStatus {

    CANCELLED,

    ACTIVE,

    DECLINED,

    IN_MODERATION;

    public static Optional<EventStatus> from(String status) {
        for (EventStatus eventStatus : EventStatus.values()) {
            if (eventStatus.name().equalsIgnoreCase(status)) {
                return Optional.of(eventStatus);
            }
        }
        return Optional.empty();
    }
}
