package ru.practicum.mainservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addUser(User user) {
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsers(List<Integer> ids) {
        return userRepository.findAllById(ids);
    }

    @Override
    public User getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Не найден пользователь id=" + id));
        return user;
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Не найден пользователь id=" + id));
        userRepository.deleteById(id);
    }
}
