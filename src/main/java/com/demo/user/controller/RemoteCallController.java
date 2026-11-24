package com.demo.user.controller;

import com.demo.user.model.OrderDto;
import com.demo.user.model.ProductDto;
import com.demo.user.service.RemoteCallService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/call")
public class RemoteCallController {

    private final RemoteCallService remote;

    public RemoteCallController(RemoteCallService remote) {
        this.remote = remote;
    }

    @GetMapping("/orders")
    public List<OrderDto> getAllOrders() {
        return remote.getAllOrders();
    }

    @GetMapping("/orders/{id}")
    public OrderDto getOrderById(@PathVariable Long id) {
        return remote.getOrderById(id);
    }

    @GetMapping("/products")
    public List<ProductDto> getAllProducts() {
        return remote.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public ProductDto getProductById(@PathVariable Long id) {
        return remote.getProductById(id);
    }

    @GetMapping("/all")
    public Map<String, Object> callAll() {
        return remote.callAll();
    }
}
