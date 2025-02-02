package com.project.prodify.output;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItemResponse(
        String sku,
        int quantity,
        String productName,
        BigDecimal subtotal) {
}