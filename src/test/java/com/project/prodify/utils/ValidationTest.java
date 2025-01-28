package com.project.prodify.utils;

import com.project.prodify.domain.Product;
import com.project.prodify.exception.ValidationException;
import com.project.prodify.input.ProductRequest;
import com.project.prodify.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidationTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private Validation validation;

    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setPrice(new BigDecimal("100.0"));
        productRequest.setStock(10);
    }

    void testValidStock() {
        validation.validStock(10);
    }

    @Test
    void testValidStockThrowsException() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validation.validStock(0);
        });

        String expectedMessage = "Stock cannot be 0 or less than 0";
        System.out.println("testValidStockThrowsException");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + exception.getMessage());

        assertEquals(expectedMessage, exception.getMessage());
    }

    void testValidQuantity() {
        validation.validQuantity(5);
    }

    @Test
    void testValidQuantityThrowsException() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validation.validQuantity(0);
        });

        String expectedMessage = "Quantity cannot be 0 or less than 0";
        System.out.println("testValidQuantityThrowsException");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + exception.getMessage());

        assertEquals(expectedMessage, exception.getMessage());
    }

    void testValidSubTotal() {
        validation.validSubTotal(new BigDecimal("10.0"));
    }

    @Test
    void testValidSubTotalThrowsException() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validation.validSubTotal(new BigDecimal("-1.0"));
        });

        String expectedMessage = "SubTotal cannot be less than 0";
        System.out.println("testValidSubTotalThrowsException");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + exception.getMessage());

        assertEquals(expectedMessage, exception.getMessage());
    }

    void testValidPrice() {
        validation.validPrice(new BigDecimal("10.0"));
    }

    @Test
    void testValidPriceThrowsException() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validation.validPrice(new BigDecimal("0.0"));
        });

        String expectedMessage = "Price cannot be 0 or less than 0";
        System.out.println("testValidPriceThrowsException");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + exception.getMessage());

        assertEquals(expectedMessage, exception.getMessage());
    }

    void testValidName() {
        when(productRepository.findByName(anyString())).thenReturn(Optional.empty());
        validation.validName("Unique Product");
    }

    @Test
    void testValidNameThrowsException() {
        when(productRepository.findByName(anyString())).thenReturn(Optional.of(new Product()));
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validation.validName("Test Product");
        });

        String expectedMessage = "You cannot have products with the same name";
        System.out.println("testValidNameThrowsException");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + exception.getMessage());

        assertEquals(expectedMessage, exception.getMessage());
    }

    void testValidNameNull() {
        validation.validNameNull("Valid Name");
    }

    @Test
    void testValidNameNullThrowsException() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validation.validNameNull("");
        });

        String expectedMessage = "Name cannot be null or empty";
        System.out.println("testValidNameNullThrowsException");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + exception.getMessage());

        assertEquals(expectedMessage, exception.getMessage());
    }

    void testValidateProductRequest() {
        when(productRepository.findByName(anyString())).thenReturn(Optional.empty());
        validation.validateProductRequest(productRequest);
    }

    @Test
    void testValidateProductRequestThrowsException() {
        productRequest.setStock(0);
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validation.validateProductRequest(productRequest);
        });

        String expectedMessage = "Stock cannot be 0 or less than 0";
        System.out.println("testValidateProductRequestThrowsException");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + exception.getMessage());

        assertEquals(expectedMessage, exception.getMessage());
    }
}