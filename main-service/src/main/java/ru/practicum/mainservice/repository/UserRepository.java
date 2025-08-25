package ru.practicum.mainservice.repository;

import ru.practicum.mainservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
