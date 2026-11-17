package com.demo.user.model;

// MODEL - CONSUMER copy of product-service's contract (intentionally NOT shared).
public record ProductDto(Long id, String name, double price) {}
