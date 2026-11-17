package com.demo.user.service;

import com.demo.user.repository.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest {

    private final UserService service = new UserService(new UserRepository());

    @Test
    void getAllReturnsTwoItems() {
        assertEquals(2, service.getAll().size());
    }

    @Test
    void getByIdFindsExistingItem() {
        assertTrue(service.getById(1L).isPresent());
    }

    @Test
    void getByIdReturnsEmptyForUnknownId() {
        assertTrue(service.getById(999999L).isEmpty());
    }
}
