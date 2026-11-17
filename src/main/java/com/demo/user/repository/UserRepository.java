package com.demo.user.repository;

import com.demo.user.model.UserDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// R of data layer: in-memory data (replace with a real DB later)
@Repository
public class UserRepository {

    private final List<UserDto> data = List.of(
            new UserDto(1L, "Amit Sharma", "amit@example.com"),
            new UserDto(2L, "Priya Singh", "priya@example.com"));

    public List<UserDto> findAll() {
        return data;
    }

    public Optional<UserDto> findById(Long id) {
        return data.stream().filter(i -> i.id().equals(id)).findFirst();
    }
}
