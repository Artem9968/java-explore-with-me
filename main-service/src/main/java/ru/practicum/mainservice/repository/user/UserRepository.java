package ru.practicum.mainservice.repository.user;

import ru.practicum.mainservice.model.user.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
