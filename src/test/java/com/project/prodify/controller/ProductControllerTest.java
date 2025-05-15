package com.project.prodify.controller;

import com.project.prodify.dto.request.ProductRequest;
import com.project.prodify.dto.response.ProductResponse;
import com.project.prodify.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setPrice(new BigDecimal("100.0"));
        productRequest.setStock(10);
    }

    @Test
    void testSaveProduct() {
        ProductResponse productResponse = ProductResponse.builder()
                .message("Product created successfully")
                .name("Test Product")
                .price(new BigDecimal("100.0"))
                .stock(10)
                .creationDate(LocalDateTime.now())
                .modificationDate(LocalDateTime.now())
                .status(true)
                .SKU("SKU123")
                .build();

        when(productService.saveProduct(any(ProductRequest.class))).thenReturn(productResponse);

        ProductResponse response = productController.saveProduct(productRequest);

        String expectedMessage = "Product created successfully";
        System.out.println("testSaveProduct");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + response.message());

        assertNotNull(response);
        assertEquals(expectedMessage, response.message());
        verify(productService, times(1)).saveProduct(any(ProductRequest.class));
    }

    @Test
    void testSaveProductFailure() {
        when(productService.saveProduct(any(ProductRequest.class))).thenThrow(new RuntimeException("Service exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productController.saveProduct(productRequest);
        });

        String expectedMessage = "Service exception";
        System.out.println("testSaveProductFailure");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + exception.getMessage());

        assertEquals(expectedMessage, exception.getMessage());
        verify(productService, times(1)).saveProduct(any(ProductRequest.class));
    }

    @Test
    void testFindProductName() {
        ProductResponse productResponse = ProductResponse.builder()
                .message("Product found successfully")
                .name("Test Product")
                .price(new BigDecimal("100.0"))
                .stock(10)
                .creationDate(LocalDateTime.now())
                .modificationDate(LocalDateTime.now())
                .status(true)
                .SKU("SKU123")
                .build();

        when(productService.findProductName(anyString())).thenReturn(productResponse);

        ProductResponse response = productController.findProductName("Test Product");

        String expectedMessage = "Product found successfully";
        System.out.println("testFindProductName");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + response.message());

        assertNotNull(response);
        assertEquals(expectedMessage, response.message());
        verify(productService, times(1)).findProductName(anyString());
    }

    @Test
    void testFindProductNameFailure() {
        when(productService.findProductName(anyString())).thenThrow(new RuntimeException("Service exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productController.findProductName("Test Product");
        });

        String expectedMessage = "Service exception";
        System.out.println("testFindProductNameFailure");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + exception.getMessage());

        assertEquals(expectedMessage, exception.getMessage());
        verify(productService, times(1)).findProductName(anyString());
    }

    @Test
    void testFindProductSKU() {
        ProductResponse productResponse = ProductResponse.builder()
                .message("Product found successfully")
                .name("Test Product")
                .price(new BigDecimal("100.0"))
                .stock(10)
                .creationDate(LocalDateTime.now())
                .modificationDate(LocalDateTime.now())
                .status(true)
                .SKU("SKU123")
                .build();

        when(productService.findProductSKU(anyString())).thenReturn(productResponse);

        ProductResponse response = productController.findProductSKU("SKU123");

        String expectedMessage = "Product found successfully";
        System.out.println("testFindProductSKU");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + response.message());

        assertNotNull(response);
        assertEquals(expectedMessage, response.message());
        verify(productService, times(1)).findProductSKU(anyString());
    }

    @Test
    void testFindProductSKUFailure() {
        when(productService.findProductSKU(anyString())).thenThrow(new RuntimeException("Service exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productController.findProductSKU("SKU123");
        });

        String expectedMessage = "Service exception";
        System.out.println("testFindProductSKUFailure");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + exception.getMessage());

        assertEquals(expectedMessage, exception.getMessage());
        verify(productService, times(1)).findProductSKU(anyString());
    }

    @Test
    void testDeleteProduct() {
        when(productService.deleteProduct(anyString())).thenReturn("Product deleted successfully");

        String response = productController.deletedProduct("SKU123");

        String expectedMessage = "Product deleted successfully";
        System.out.println("testDeleteProduct");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + response);

        assertEquals(expectedMessage, response);
        verify(productService, times(1)).deleteProduct(anyString());
    }

    @Test
    void testDeleteProductFailure() {
        when(productService.deleteProduct(anyString())).thenThrow(new RuntimeException("Service exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productController.deletedProduct("SKU123");
        });

        String expectedMessage = "Service exception";
        System.out.println("testDeleteProductFailure");
        System.out.println("Expectativa: " + expectedMessage);
        System.out.println("Response: " + exception.getMessage());

        assertEquals(expectedMessage, exception.getMessage());
        verify(productService, times(1)).deleteProduct(anyString());
    }
}