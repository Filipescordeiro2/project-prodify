package com.project.prodify.domain;

import com.project.prodify.dto.request.OrderRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(nullable = false)
    private List<OrderItem> items;

    private BigDecimal total;
    private LocalDateTime purchaseDate;

    private BigDecimal calculateTotal() {
        return items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @PrePersist
    public void prePersist() {
        this.purchaseDate = LocalDateTime.now();
        this.total = calculateTotal();
    }

    public Order(OrderRequest request, List<OrderItem> items) {
        this.items = items;
        this.total = calculateTotal();
        for (OrderItem item : items) {
            item.setOrder(this);
        }
    }
}