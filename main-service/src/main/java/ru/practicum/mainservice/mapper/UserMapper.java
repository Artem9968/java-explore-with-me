package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.user.UserDto;
import ru.practicum.mainservice.dto.user.UserShortDto;
import ru.practicum.mainservice.model.user.User;

public class UserMapper {

    public static UserShortDto toUserShortDto(User entity) {
        UserShortDto result = new UserShortDto();
        result.setId(entity.getId());
        result.setName(entity.getName());
        return result;
    }

    public static UserDto toUserDto(User entity) {
        UserDto result = new UserDto();
        result.setId(entity.getId());
        result.setName(entity.getName());
        result.setEmail(entity.getEmail());
        return result;
    }

    public static User toUser(UserDto userDto) {
        User result = new User();
        if (userDto.getId() != null) {
            result.setId(userDto.getId());
        }
        result.setName(userDto.getName());
        result.setEmail(userDto.getEmail());
        return result;
    }
}
