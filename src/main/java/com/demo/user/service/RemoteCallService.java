package com.demo.user.service;

import com.demo.user.model.OrderDto;
import com.demo.user.model.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Calls EVERY endpoint of the other two services using RestTemplate.
 * If any of them changes its URL / fields / return type, these calls fail.
 */
@Service
public class RemoteCallService {

    private final RestTemplate rest;
    private final String orderUrl;
    private final String productUrl;

    public RemoteCallService(RestTemplate rest, @Value("${order.service.url}") String orderUrl, @Value("${product.service.url}") String productUrl) {
        this.rest = rest;
        this.orderUrl = orderUrl;
        this.productUrl = productUrl;
    }

    // ---- calls order-service (port 8081) ----
    public List<OrderDto> getAllOrders() {
        return rest.exchange(orderUrl + "/api/orders", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<OrderDto>>() {}).getBody();
    }

    public OrderDto getOrderById(Long id) {
        return rest.getForObject(orderUrl + "/api/orders/" + id, OrderDto.class);
    }

    // ---- calls product-service (port 8082) ----
    public List<ProductDto> getAllProducts() {
        return rest.exchange(productUrl + "/api/products", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ProductDto>>() {}).getBody();
    }

    public ProductDto getProductById(Long id) {
        return rest.getForObject(productUrl + "/api/products/" + id, ProductDto.class);
    }

    // ---- calls every endpoint of both other services ----
    public Map<String, Object> callAll() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("orders", getAllOrders());
        result.put("products", getAllProducts());
        result.put("orderById", getOrderById(100L));
        result.put("productById", getProductById(500L));
        return result;
    }
}
