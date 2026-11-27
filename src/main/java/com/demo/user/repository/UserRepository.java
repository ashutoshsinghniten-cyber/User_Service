package com.demo.user.repository;

import com.demo.user.model.AddressDto;
import com.demo.user.model.TagDto;
import com.demo.user.model.UserDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// R of data layer: in-memory data (replace with a real DB later)
@Repository
public class UserRepository {

    private final List<UserDto> data = List.of(
            new UserDto(1L, "Amit",  "amit@example.com",
                    new AddressDto("12 MG Road",    "Mumbai", "Maharashtra", "400001"),
                    List.of("+91-9876543210", "+91-9123456789"),
                    List.of(new TagDto("role", "admin"), new TagDto("plan", "premium"))),
            new UserDto(2L, "Priya", "priya@example.com",
                    new AddressDto("45 Park Street", "Delhi",  "Delhi",       "110001"),
                    List.of("+91-9000012345"),
                    List.of(new TagDto("role", "customer"), new TagDto("plan", "free"))));

    public List<UserDto> findAll() {
        return data;
    }

    public Optional<UserDto> findById(Long id) {
        return data.stream().filter(i -> i.id().equals(id)).findFirst();
    }
}
