package com.project.prodify.domain;

import com.project.prodify.dto.request.OrderItemRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_items")
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal subtotal;

    public OrderItem(OrderItemRequest itemRequest, Product product) {
        this.product = product;
        this.quantity = itemRequest.getQuantity();
        this.subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
    }
}