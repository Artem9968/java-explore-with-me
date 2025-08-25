package ru.practicum.mainservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.dto.UserDto;
import ru.practicum.mainservice.mapper.UserMapper;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.service.UserService;


import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/users")
public class AdminUsersController {
    private final UserService userService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers(
            @RequestParam(name = "ids", required = false) List<Integer> ids,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Администратор запрашивает  запрашивает список пользователей. {}", ids);
        List<User> users;
        if (ids != null) {
            users = userService.getUsers(ids);
        } else {
            users = userService.getUsers();
        }
        return users.stream()
                .map(UserMapper::toUserDto)
                .skip(from)
                .limit(size)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable Integer id) {
        log.info("Выполняем поиск пользователя id={}.", id);
        User user = userService.getUserById(id);
        return UserMapper.toUserDto(user);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Validated @RequestBody UserDto userDto) {
        log.info("Создаем нового пользователя {}", userDto.toString());
        User savedUser = userService.addUser(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(savedUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer id) {
        log.info("Удаляем пользователя {}", id);
        userService.deleteUser(id);
    }

}
