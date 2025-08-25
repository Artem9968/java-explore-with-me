package ru.practicum.mainservice.model.enums;

import java.util.Optional;

public enum RequestState {

    DECLINED,

    CANCELLED,

    APPROVED,

    IN_REVIEW;

    public static Optional<RequestState> from(String status) {
        for (RequestState requestState : RequestState.values()) {
            if (requestState.name().equalsIgnoreCase(status)) {
                return Optional.of(requestState);
            }
        }
        return Optional.empty();
    }
}
