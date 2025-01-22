package com.project.prodify.output;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderItemResponse(
        String SKU,
        int quantity,
        BigDecimal subtotal) {
}
