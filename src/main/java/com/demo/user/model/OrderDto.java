package com.demo.user.model;

// MODEL - CONSUMER copy of order-service's contract (intentionally NOT shared).
public record OrderDto(Long id, Long customerId, Long itemId, double quantity, String status, String createdAt) {}
