package com.demo.user.service;

import com.demo.user.model.UserDto;
import com.demo.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Business logic of this service's own data
@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<UserDto> getAll() {
        return repository.findAll();
    }

    // public Optional<UserDto> getById(Long id) {
         return repository.findById(id);
    // }
}
