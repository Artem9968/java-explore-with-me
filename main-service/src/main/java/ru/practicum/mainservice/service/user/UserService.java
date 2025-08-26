package ru.practicum.mainservice.service.user;

import java.util.List;
import ru.practicum.mainservice.model.user.User;

public interface UserService {
    
    void removeUser(Integer userId);

    User createUser(User user);

    User findUserById(Integer userId);

    List<User> findUsersByIds(List<Integer> userIds);

    List<User> findAllUsers();
}
