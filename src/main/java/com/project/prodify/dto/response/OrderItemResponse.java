package com.project.prodify.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItemResponse(
        String sku,
        int quantity,
        String productName,
        BigDecimal subtotal) {
}