package com.demo.user.model;

import java.util.List;

// MODEL - PROVIDER contract (this service owns it). Changing it breaks the other two services.
// BREAKING CHANGES (Level 4):
//   1. phoneNumbers added as List<String>
//   2. tags added as List<TagDto> (list of nested objects)
public record UserDto(Long id, String name, String email, AddressDto address, List<String> phoneNumbers, List<TagDto> tags) {}
