package ru.practicum.mainservice.service.user;

import ru.practicum.mainservice.storage.user.UserRepository;
import ru.practicum.mainservice.model.user.User;
import ru.practicum.mainservice.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void removeUser(Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        userRepository.deleteById(userId);
    }

    @Override
    public User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    @Override
    public List<User> findUsersByIds(List<Integer> userIds) {
        return userRepository.findAllById(userIds);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }
}
