package com.project.prodify.utils;

import com.project.prodify.exception.ValidationException;
import com.project.prodify.input.ProductRequest;
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

    private final ProductRepository repository;

    public void validateProductRequest(ProductRequest request) {
        validStock(request.getStock());
        validPrice(request.getPrice());
        validName(request.getName());
        validNameNull(request.getName());
    }

    public void validStock(int stock) {
        if (stock <= 0) {
            log.error("Stock validation failed: stock cannot be 0 or less than 0 --> " + stock);
            throw new ValidationException("Stock cannot be 0 or less than 0");
        }
    }

    public void validQuantity(int quantity) {
        if (quantity <= 0) {
            log.error("Quantity validation failed: quantity cannot be 0 or less than 0 --> " + quantity);
            throw new ValidationException("Quantity cannot be 0 or less than 0");
        }
    }

    public void validSubTotal(BigDecimal subtotal) {
        if (subtotal.compareTo(BigDecimal.ZERO) < 0) {
            log.error("Subtotal validation failed: subtotal cannot be less than 0 --> " + subtotal);
            throw new ValidationException("SubTotal cannot be less than 0");
        }
    }

    public void validPrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Price validation failed: price cannot be 0 or less than 0 --> " + price);
            throw new ValidationException("Price cannot be 0 or less than 0");
        }
    }

    public void validName(String name) {
        var product = repository.findByName(name);
        if (product.isPresent()) {
            log.error("Name validation failed: product with the same name already exists");
            throw new ValidationException("You cannot have products with the same name");
        }
    }
    public void validNameNull(String name){
        if (isEmpty(name) || name.equals("")){
            log.error("Name validation failed: name cannot be null or empty");
            throw new ValidationException("Name cannot be null or empty");
        }
    }
}