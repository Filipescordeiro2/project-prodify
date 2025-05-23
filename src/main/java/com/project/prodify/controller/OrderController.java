package com.project.prodify.controller;

import com.project.prodify.dto.request.OrderRequest;
import com.project.prodify.dto.response.OrderResponse;
import com.project.prodify.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse getOrderById(@PathVariable Long id) {
        return orderService.findOrderById(id);
    }

    @GetMapping("/SKU/{SKU}")
    @ResponseStatus(HttpStatus.OK)
    public Page<OrderResponse> getOrdersBySKU(@PathVariable String SKU, Pageable pageable) {
        return orderService.findOrdersBySKUProduct(SKU, pageable);
    }
}