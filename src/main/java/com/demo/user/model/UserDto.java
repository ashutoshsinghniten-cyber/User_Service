package com.demo.user.model;

// MODEL - PROVIDER contract (this service owns it). Changing it breaks the other two services.
public record UserDto(Long id, int name, String email) {}
