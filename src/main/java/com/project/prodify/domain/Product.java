package com.project.prodify.domain;

import com.project.prodify.input.ProductRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @Column(name = "product_name",unique = true)
        private String name;
        @Column(name = "product_sku",unique = true)
        private String SKU;
        @Column(name = "product_price")
        private BigDecimal price;
        @Column(name = "product_stock")
        private int stock;
        private LocalDateTime creationDate;
        private LocalDateTime modificationDate;
        private boolean status;

    @PrePersist
    public void prePersist(){
        this.creationDate = LocalDateTime.now();
        this.modificationDate = LocalDateTime.now();
        this.SKU = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
    }

    @PreUpdate
    public void preUpdate(){
        this.modificationDate = LocalDateTime.now();
    }

    public Product(ProductRequest request){
        this.name = request.getName();
        this.price = request.getPrice();
        this.stock = request.getStock();
    }

}
