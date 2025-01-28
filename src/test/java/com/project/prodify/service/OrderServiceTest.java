package com.project.prodify.service;

import com.project.prodify.domain.Order;
import com.project.prodify.domain.OrderItem;
import com.project.prodify.domain.Product;
import com.project.prodify.exception.OrderValidationException;
import com.project.prodify.input.OrderRequest;
import com.project.prodify.input.OrderItemRequest;
import com.project.prodify.output.OrderResponse;
import com.project.prodify.repository.OrderRepository;
import com.project.prodify.repository.ProductRepository;
import com.project.prodify.utils.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Validation validation;

    @InjectMocks
    private OrderService orderService;

    private OrderRequest orderRequest;
    private Order order;
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(new BigDecimal("100.0"));
        product.setStock(10);
        product.setSKU("SKU123");

        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderItemRequest.setProductId(1L);
        orderItemRequest.setQuantity(2);

        orderRequest = new OrderRequest();
        orderRequest.setItems(Collections.singletonList(orderItemRequest));

        OrderItem orderItem = new OrderItem(orderItemRequest, product);
        order = new Order(orderRequest, Collections.singletonList(orderItem));
    }

    @Test
    void testCreateOrder() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponse response = orderService.createOrder(orderRequest);

        String expectedMessage = "Order created successfully";
        System.out.println("testCreateOrder");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + response.message());

        assertNotNull(response);
        assertEquals(expectedMessage, response.message());
        assertEquals(order.getId(), response.id());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCreateOrderProductNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        OrderValidationException exception = assertThrows(OrderValidationException.class, () -> {
            orderService.createOrder(orderRequest);
        });

        String expectedMessage = "Product not found";
        System.out.println("testCreateOrderProductNotFound");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + exception.getMessage());

        assertEquals(expectedMessage, exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testFindOrderById() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        OrderResponse response = orderService.findOrderById(1L);

        String expectedMessage = "Order found successfully";
        System.out.println("testFindOrderById");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + response.message());

        assertNotNull(response);
        assertEquals(expectedMessage, response.message());
        assertEquals(order.getId(), response.id());
        verify(orderRepository, times(1)).findById(anyLong());
    }

    @Test
    void testFindOrderByIdNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        OrderValidationException exception = assertThrows(OrderValidationException.class, () -> {
            orderService.findOrderById(1L);
        });

        String expectedMessage = "Order not found";
        System.out.println("testFindOrderByIdNotFound");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + exception.getMessage());

        assertEquals(expectedMessage, exception.getMessage());
        verify(orderRepository, times(1)).findById(anyLong());
    }

    @Test
    void testFindOrdersBySKUProduct() {
        Page<Order> orders = new PageImpl<>(Collections.singletonList(order));
        when(orderRepository.findAll(any(PageRequest.class))).thenReturn(orders);

        Page<OrderResponse> response = orderService.findOrdersBySKUProduct("SKU123", PageRequest.of(0, 10));

        String expectedMessage = "Orders found successfully";
        System.out.println("testFindOrdersBySKUProduct");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + response.getContent().get(0).message());

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(expectedMessage, response.getContent().get(0).message());
        verify(orderRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testFindOrdersBySKUProductNotFound() {
        Page<Order> orders = new PageImpl<>(Collections.emptyList());
        when(orderRepository.findAll(any(PageRequest.class))).thenReturn(orders);

        OrderValidationException exception = assertThrows(OrderValidationException.class, () -> {
            orderService.findOrdersBySKUProduct("NonexistentSKU", PageRequest.of(0, 10));
        });

        String expectedMessage = "No orders with product SKU found";
        System.out.println("testFindOrdersBySKUProductNotFound");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + exception.getMessage());

        assertEquals(expectedMessage, exception.getMessage());
        verify(orderRepository, times(1)).findAll(any(PageRequest.class));
    }
}