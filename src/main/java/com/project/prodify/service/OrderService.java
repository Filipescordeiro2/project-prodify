package com.project.prodify.service;

import com.project.prodify.domain.Order;
import com.project.prodify.domain.OrderItem;
import com.project.prodify.domain.Product;
import com.project.prodify.exception.OrderValidationException;
import com.project.prodify.dto.request.OrderRequest;
import com.project.prodify.dto.response.OrderItemResponse;
import com.project.prodify.dto.response.OrderResponse;
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

    private static final String ORDER_CREATED_SUCCESS = "Order created successfully";
    private static final String ORDER_FOUND_SUCCESS = "Order found successfully";
    private static final String ORDERS_FOUND_SUCCESS = "Orders found successfully";
    private static final String ORDER_NOT_FOUND = "Order not found";
    private static final String PRODUCT_NOT_FOUND = "Product not found";
    private static final String PRODUCT_INACTIVE = "Product is inactive ";
    private static final String INSUFFICIENT_STOCK = "Insufficient stock for product: ";
    private static final String RUNTIME_EXCEPTION = "Runtime exception occurred while creating order";

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final Validation validation;

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        try {
            List<OrderItem> orderItems = searchProduct(orderRequest);
            Order order = new Order(orderRequest, orderItems);
            order = orderRepository.save(order);
            OrderResponse orderResponse = mapToOrderResponse(order, ORDER_CREATED_SUCCESS);
            log.info(ORDER_CREATED_SUCCESS + ": {}", orderResponse);
            return orderResponse;
        } catch (OrderValidationException e) {
            log.error("Error creating order", e);
            throw e;
        } catch (Exception e) {
            log.error(RUNTIME_EXCEPTION, e);
            throw new OrderValidationException(RUNTIME_EXCEPTION);
        }
    }

    public OrderResponse findOrderById(Long id) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderValidationException(ORDER_NOT_FOUND));
        return mapToOrderResponse(order, ORDER_FOUND_SUCCESS);
    }

    public Page<OrderResponse> findOrdersBySKUProduct(String SKU, Pageable pageable) {
        var orders = orderRepository.findAll(pageable);
        List<OrderResponse> matchingOrders = orders.stream()
                .filter(order -> order.getItems().stream()
                        .anyMatch(item -> item.getProduct().getSKU().equals(SKU)))
                .map(order -> mapToOrderResponse(order, ORDERS_FOUND_SUCCESS))
                .collect(Collectors.toList());

        if (matchingOrders.isEmpty()) {
            throw new OrderValidationException("No orders with product SKU found");
        }
        return new PageImpl<>(matchingOrders, pageable, matchingOrders.size());
    }

    private OrderResponse mapToOrderResponse(Order order, String message) {
        List<OrderItemResponse> itemResponses = order.getItems().stream().map(orderItem -> {
            return OrderItemResponse.builder()
                    .sku(orderItem.getProduct().getSKU())
                    .quantity(orderItem.getQuantity())
                    .productName(orderItem.getProduct().getName())
                    .subtotal(orderItem.getSubtotal())
                    .build();
        }).collect(Collectors.toList());

        return OrderResponse.builder()
                .message(message)
                .id(order.getId())
                .items(itemResponses)
                .total(order.getTotal())
                .purchaseDate(order.getPurchaseDate())
                .build();
    }

    private List<OrderItem> searchProduct(OrderRequest orderRequest) {
        return orderRequest.getItems().stream().map(itemRequest -> {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new OrderValidationException(PRODUCT_NOT_FOUND));
            if (!product.isStatus()) {
                log.error(PRODUCT_INACTIVE + product.getName());
                throw new OrderValidationException(PRODUCT_INACTIVE + product.getSKU());
            }
            if (product.getStock() < itemRequest.getQuantity()) {
                log.error(INSUFFICIENT_STOCK + product.getName());
                throw new OrderValidationException(INSUFFICIENT_STOCK + product.getSKU());
            }
            validation.validateQuantity(itemRequest.getQuantity());
            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);
            return new OrderItem(itemRequest, product);
        }).collect(Collectors.toList());
    }
}