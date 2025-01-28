package com.project.prodify.service;

import com.project.prodify.domain.Order;
import com.project.prodify.domain.OrderItem;
import com.project.prodify.domain.Product;
import com.project.prodify.exception.OrderValidationException;
import com.project.prodify.input.OrderRequest;
import com.project.prodify.output.OrderItemResponse;
import com.project.prodify.output.OrderResponse;
import com.project.prodify.repository.OrderRepository;
import com.project.prodify.repository.ProductRepository;
import com.project.prodify.utils.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final Validation validation;

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        try {
            List<OrderItem> orderItems = searchProduct(orderRequest);
            Order order = new Order(orderRequest, orderItems);
            order = orderRepository.save(order);
            OrderResponse orderResponse = mapToOrderResponse(order);
            log.info("Order created successfully: {}", orderResponse);
            return orderResponse;
        } catch (OrderValidationException e) {
            log.error("Runtime exception occurred while creating order", e);
            throw new OrderValidationException("Runtime exception occurred while creating order");
        } catch (Exception e) {
            log.error("Error creating order", e);
            throw new OrderValidationException("Error creating order");
        }
    }

    public OrderResponse findOrderById(Long id) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderValidationException("Order not found"));
        return mapToOrderResponse(order);
    }

    public Page<OrderResponse> findOrdersBySKUProduct(String SKU, Pageable pageable) {
        var orders = orderRepository.findAll(pageable);
        List<OrderResponse> matchingOrders = orders.stream()
                .filter(order -> order.getItems().stream()
                        .anyMatch(item -> item.getProduct().getSKU().equals(SKU)))
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());

        if (matchingOrders.isEmpty()) {
            throw new OrderValidationException("No orders with product SKU found");
        }
        return new PageImpl<>(matchingOrders, pageable, matchingOrders.size());
    }

    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream().map(orderItem -> {
            return OrderItemResponse.builder()
                    .SKU(orderItem.getProduct().getSKU())
                    .quantity(orderItem.getQuantity())
                    .subtotal(orderItem.getSubtotal())
                    .build();
        }).collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .items(itemResponses)
                .total(order.getTotal())
                .purchaseDate(order.getPurchaseDate())
                .build();
    }

    private List<OrderItem> searchProduct(OrderRequest orderRequest) {
        return orderRequest.getItems().stream().map(itemRequest -> {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new OrderValidationException("Product not found"));
            if (product.getStock() < itemRequest.getQuantity()) {
                log.error("Insufficient stock for product: {}", product.getName());
                throw new OrderValidationException("Insufficient stock for product: " + product.getName());
            }
            validation.validQuantity(itemRequest.getQuantity());
            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);
            return new OrderItem(itemRequest, product);
        }).collect(Collectors.toList());
    }
}