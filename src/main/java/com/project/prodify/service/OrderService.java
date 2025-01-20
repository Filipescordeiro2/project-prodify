package com.project.prodify.service;

import com.project.prodify.domain.Order;
import com.project.prodify.domain.OrderItem;
import com.project.prodify.domain.Product;
import com.project.prodify.input.OrderRequest;
import com.project.prodify.output.OrderItemResponse;
import com.project.prodify.output.OrderResponse;
import com.project.prodify.repository.OrderItemRepository;
import com.project.prodify.repository.OrderRepository;
import com.project.prodify.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private  final ProductRepository productRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        List<OrderItem> orderItems = searchProduct(orderRequest);
        Order order = new Order(orderRequest, orderItems);
        order = orderRepository.save(order);
        return mapToOrderResponse(order);
    }
    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream().map(orderItem -> {
            return OrderItemResponse.builder()
                    .productId(orderItem.getProduct().getId())
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
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            if (product.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);
            return new OrderItem(itemRequest, product);
        }).collect(Collectors.toList());
    }
}

