package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.UserResponse;
import ru.practicum.mainservice.dto.UserShortDto;
import ru.practicum.mainservice.model.User;

public class UserMapper {
    private UserMapper() {
    }

    public static User toUser(UserResponse dto) {
        User user = new User();
        if (dto.getId() != null) {
            user.setId(dto.getId());
        }
        user.setName(dto.getName());
        user.setEmailAddress(dto.getEmailAddress());
        return user;
    }

    public static UserResponse toUserDto(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmailAddress(user.getEmailAddress());
        return dto;
    }

    public static UserShortDto toUserShortDto(User user) {
        UserShortDto dto = new UserShortDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        return dto;
    }
}
