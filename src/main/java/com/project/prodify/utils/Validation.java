package com.project.prodify.utils;

import com.project.prodify.exception.ValidationException;
import com.project.prodify.dto.request.ProductRequest;
import com.project.prodify.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
@RequiredArgsConstructor
@Slf4j
public class Validation {

    private static final String STOCK_VALIDATION_FAILED = "Stock validation failed: stock cannot be 0 or less than 0 --> ";
    private static final String QUANTITY_VALIDATION_FAILED = "Quantity validation failed: quantity cannot be 0 or less than 0 --> ";
    private static final String SUBTOTAL_VALIDATION_FAILED = "Subtotal validation failed: subtotal cannot be less than 0 --> ";
    private static final String PRICE_VALIDATION_FAILED = "Price validation failed: price cannot be 0 or less than 0 --> ";
    private static final String NAME_VALIDATION_FAILED = "Name validation failed: product with the same name already exists";
    private static final String NAME_NULL_VALIDATION_FAILED = "Name validation failed: name cannot be null or empty";

    private final ProductRepository repository;

    public void validateProductRequest(ProductRequest request) {
        validateStock(request.getStock());
        validatePrice(request.getPrice());
        validateName(request.getName());
        validateNameNotNull(request.getName());
    }

    public void validateProductRequestUpdate(ProductRequest request) {
        validateStock(request.getStock());
        validatePrice(request.getPrice());
        validateNameNotNull(request.getName());
    }

    public void validateStock(int stock) {
        if (stock <= 0) {
            log.error(STOCK_VALIDATION_FAILED + stock);
            throw new ValidationException("Stock cannot be 0 or less than 0");
        }
    }

    public void validateQuantity(int quantity) {
        if (quantity <= 0) {
            log.error(QUANTITY_VALIDATION_FAILED + quantity);
            throw new ValidationException("Quantity cannot be 0 or less than 0");
        }
    }

    public void validateSubTotal(BigDecimal subtotal) {
        if (subtotal.compareTo(BigDecimal.ZERO) < 0) {
            log.error(SUBTOTAL_VALIDATION_FAILED + subtotal);
            throw new ValidationException("SubTotal cannot be less than 0");
        }
    }

    public void validatePrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            log.error(PRICE_VALIDATION_FAILED + price);
            throw new ValidationException("Price cannot be 0 or less than 0");
        }
    }

    public void validateName(String name) {
        var product = repository.findByName(name);
        if (product.isPresent()) {
            log.error(NAME_VALIDATION_FAILED);
            throw new ValidationException("You cannot have products with the same name");
        }
    }

    public void validateNameNotNull(String name) {
        if (isEmpty(name) || name.equals("")) {
            log.error(NAME_NULL_VALIDATION_FAILED);
            throw new ValidationException("Name cannot be null or empty");
        }
    }
}