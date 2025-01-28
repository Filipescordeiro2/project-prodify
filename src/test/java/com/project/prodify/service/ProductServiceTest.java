package com.project.prodify.service;

import com.project.prodify.domain.Product;
import com.project.prodify.exception.ProductValidationException;
import com.project.prodify.exception.ValidationException;
import com.project.prodify.input.ProductRequest;
import com.project.prodify.output.ProductResponse;
import com.project.prodify.repository.ProductRepository;
import com.project.prodify.utils.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Validation validation;

    @InjectMocks
    private ProductService productService;

    private ProductRequest productRequest;
    private Product product;

    @BeforeEach
    void setUp() {
        productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setPrice(new BigDecimal("100.0"));
        productRequest.setStock(10);
        productRequest.setStatus(true);

        product = new Product(productRequest);
    }

    @Test
    void testSaveProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.saveProduct(productRequest);

        String expectedMessage = "Product created successfully";
        System.out.println("testSaveProduct");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + response.message());

        assertNotNull(response);
        assertEquals(expectedMessage, response.message());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testSaveProductValidationException() {
        doThrow(new ValidationException("Invalid product request")).when(validation).validateProductRequest(any(ProductRequest.class));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            productService.saveProduct(productRequest);
        });

        String expectedMessage = "Invalid product request";
        System.out.println("testSaveProductValidationException");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + exception.getMessage());

        assertEquals(expectedMessage, exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testFindProductName() {
        when(productRepository.findByName(anyString())).thenReturn(Optional.of(product));

        ProductResponse response = productService.findProductName("Test Product");

        String expectedMessage = "Product found successfully";
        System.out.println("testFindProductName");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + response.message());

        assertNotNull(response);
        assertEquals(expectedMessage, response.message());
        verify(productRepository, times(1)).findByName(anyString());
    }

    @Test
    void testFindProductNameNotFound() {
        when(productRepository.findByName(anyString())).thenReturn(Optional.empty());

        ProductValidationException exception = assertThrows(ProductValidationException.class, () -> {
            productService.findProductName("Nonexistent Product");
        });

        String expectedMessage = "Product not found with identifier: Nonexistent Product";
        System.out.println("testFindProductNameNotFound");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + exception.getMessage());

        assertEquals(expectedMessage, exception.getMessage());
        verify(productRepository, times(1)).findByName(anyString());
    }

    @Test
    void testFindProductSKU() {
        when(productRepository.findBySKU(anyString())).thenReturn(Optional.of(product));

        ProductResponse response = productService.findProductSKU("SKU123");

        String expectedMessage = "Product found successfully";
        System.out.println("testFindProductSKU");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + response.message());

        assertNotNull(response);
        assertEquals(expectedMessage, response.message());
        verify(productRepository, times(1)).findBySKU(anyString());
    }

    @Test
    void testFindProductSKUNotFound() {
        when(productRepository.findBySKU(anyString())).thenReturn(Optional.empty());

        ProductValidationException exception = assertThrows(ProductValidationException.class, () -> {
            productService.findProductSKU("NonexistentSKU");
        });

        String expectedMessage = "Product not found with identifier: NonexistentSKU";
        System.out.println("testFindProductSKUNotFound");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + exception.getMessage());

        assertEquals(expectedMessage, exception.getMessage());
        verify(productRepository, times(1)).findBySKU(anyString());
    }

    @Test
    void testUpdateProduct() {
        when(productRepository.findBySKU(anyString())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.updateProduct("SKU123", productRequest);

        String expectedMessage = "Product updated successfully";
        System.out.println("testUpdateProduct");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + response.message());

        assertNotNull(response);
        assertEquals(expectedMessage, response.message());
        verify(productRepository, times(1)).findBySKU(anyString());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProductValidationException() {
        doThrow(new ValidationException("Invalid product request")).when(validation).validateProductRequest(any(ProductRequest.class));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            productService.updateProduct("SKU123", productRequest);
        });

        String expectedMessage = "Invalid product request";
        System.out.println("testUpdateProductValidationException");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + exception.getMessage());

        assertEquals(expectedMessage, exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.findBySKU(anyString())).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(any(Product.class));

        String response = productService.deleteProduct("SKU123");

        String expectedMessage = "Product deleted successfully --> " + product;
        System.out.println("testDeleteProduct");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + response);

        assertEquals(expectedMessage, response);
        verify(productRepository, times(1)).findBySKU(anyString());
        verify(productRepository, times(1)).delete(any(Product.class));
    }

    @Test
    void testDeleteProductNotFound() {
        when(productRepository.findBySKU(anyString())).thenReturn(Optional.empty());

        ProductValidationException exception = assertThrows(ProductValidationException.class, () -> {
            productService.deleteProduct("NonexistentSKU");
        });

        String expectedMessage = "Error in deleted for product --> NonexistentSKU";
        System.out.println("testDeleteProductNotFound");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + exception.getMessage());

        assertEquals(expectedMessage, exception.getMessage());
        verify(productRepository, times(1)).findBySKU(anyString());
        verify(productRepository, never()).delete(any(Product.class));
    }
}