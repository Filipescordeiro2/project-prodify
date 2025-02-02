package com.project.prodify.output;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record ProductResponse(
        String message,
        String name,
        BigDecimal price,
        int stock,
        LocalDateTime creationDate,
        LocalDateTime modificationDate,
        boolean status,
        String sku) {
}