package com.project.prodify.output;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderResponse(
        Long id,
        List<OrderItemResponse> items,
        BigDecimal total,
        LocalDateTime purchaseDate) {
}
