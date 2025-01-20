package com.project.prodify.domain;

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

    @OneToMany
    @JoinColumn(name = "orderItem_id")
    @Column(nullable = false)
    private List<OrderItem>items;

    private BigDecimal total;
    private LocalDateTime purchaseDate;

    @PrePersist
    public void prePersist(){
        this.purchaseDate = LocalDateTime.now();
    }


}
