package ru.practicum.mainservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.dto.user.UserDto;
import ru.practicum.mainservice.mapper.UserMapper;
import ru.practicum.mainservice.model.user.User;
import ru.practicum.mainservice.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUsersController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> listUsers(
            @RequestParam(name = "ids", required = false) List<Integer> userIds,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("Администратор получает список пользователей. userIds={}", userIds);
        List<User> users = (userIds != null) ? userService.findUsersByIds(userIds) : userService.findAllUsers();

        return users.stream()
                .map(UserMapper::toUserDto)
                .skip(from)
                .limit(size)
                .toList();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable Integer userId) {
        log.info("Ищем пользователя с id={}.", userId);
        User user = userService.findUserById(userId);
        return UserMapper.toUserDto(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Validated @RequestBody UserDto newUserDto) {
        log.info("Добавляем нового пользователя: {}", newUserDto);
        return UserMapper.toUserDto(userService.createUser(UserMapper.toUser(newUserDto)));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable Integer userId) {
        log.info("Пользователь с id={} удаляется администратором.", userId);
        userService.removeUser(userId);
    }
}


