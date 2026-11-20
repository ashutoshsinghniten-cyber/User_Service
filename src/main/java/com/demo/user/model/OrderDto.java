package com.demo.user.model;

// MODEL - CONSUMER copy of order-service's contract (intentionally NOT shared).
public record OrderDto(Long id, Long userId, Long productId, int quantity, int Age) {}
