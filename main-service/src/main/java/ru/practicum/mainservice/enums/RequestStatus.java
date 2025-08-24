package ru.practicum.mainservice.enums;

import java.util.Optional;

public enum RequestStatus {
    PENDING,
    CONFIRMED,
    REJECTED,
    CANCELED;

    public static Optional<RequestStatus> from(String state) {
        for (RequestStatus status : RequestStatus.values()) {
            if (status.name().equalsIgnoreCase(state)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}
