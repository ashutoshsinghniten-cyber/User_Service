package com.demo.user.model;

// MODEL - PROVIDER contract (this service owns it). Changing it breaks the other two services.
// BREAKING CHANGE (Level 3): address field added as nested object (AddressDto)
public record UserDto(Long id, String name, String email, AddressDto address) {}
