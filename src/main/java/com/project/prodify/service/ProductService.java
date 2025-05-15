package com.project.prodify.service;

import com.project.prodify.domain.Product;
import com.project.prodify.exception.ProductValidationException;
import com.project.prodify.exception.ValidationException;
import com.project.prodify.dto.request.ProductRequest;
import com.project.prodify.dto.response.ProductResponse;
import com.project.prodify.repository.ProductRepository;
import com.project.prodify.utils.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private static final String PRODUCT_CREATED_SUCCESS = "Product created successfully";
    private static final String PRODUCT_FOUND_SUCCESS = "Product found successfully";
    private static final String PRODUCT_UPDATED_SUCCESS = "Product updated successfully";
    private static final String PRODUCT_STATUS_CHANGED = "Product status changed successfully";
    private static final String PRODUCT_DELETED_SUCCESS = "Product deleted successfully";
    private static final String PRODUCT_NOT_FOUND = "Product not found with identifier: ";
    private static final String ERROR_CREATING_PRODUCT = "Error creating product: ";
    private static final String ERROR_UPDATING_PRODUCT = "Error updating product: ";
    private static final String ERROR_DELETING_PRODUCT = "Error deleting product: ";

    private final ProductRepository productRepository;
    private final Validation validation;

    public ProductResponse saveProduct(ProductRequest request) {
        try {
            validation.validateProductRequest(request);
            var product = new Product(request);
            var productSaved = productRepository.save(product);
            var productResponse = buildProductResponse(productSaved, PRODUCT_CREATED_SUCCESS);
            log.info(PRODUCT_CREATED_SUCCESS + ": {}", productResponse);
            return productResponse;
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        } catch (Exception e) {
            log.error(ERROR_CREATING_PRODUCT, e);
            throw new ProductValidationException(ERROR_CREATING_PRODUCT + e.getMessage());
        }
    }

    public ProductResponse findProductName(String name) {
        var product = findProduct(name, productRepository::findByName);
        return buildProductResponse(product, PRODUCT_FOUND_SUCCESS);
    }

    public ProductResponse findProductSKU(String SKU) {
        var product = findProduct(SKU, productRepository::findBySKU);
        return buildProductResponse(product, PRODUCT_FOUND_SUCCESS);
    }

    public ProductResponse updateStatusProduct(String SKU, Boolean status) {
        try {
            var product = findProduct(SKU, productRepository::findBySKU);
            updateProductStatus(product, status);
            var productSaved = productRepository.save(product);
            var productResponse = buildProductResponse(productSaved, PRODUCT_STATUS_CHANGED);
            log.info(PRODUCT_STATUS_CHANGED + ": {}", productResponse);
            return productResponse;
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        } catch (Exception e) {
            log.error(ERROR_UPDATING_PRODUCT, e);
            throw new ProductValidationException(ERROR_UPDATING_PRODUCT);
        }
    }

    public ProductResponse updateProduct(String SKU, ProductRequest request) {
        try {
            var product = findProduct(SKU, productRepository::findBySKU);
            validation.validateProductRequestUpdate(request);
            updateProductDetails(product, request);
            var productSaved = productRepository.save(product);
            var productResponse = buildProductResponse(productSaved, PRODUCT_UPDATED_SUCCESS);
            log.info(PRODUCT_UPDATED_SUCCESS + ": {}", productResponse);
            return productResponse;
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        } catch (Exception e) {
            log.error(ERROR_UPDATING_PRODUCT, e);
            throw new ProductValidationException(ERROR_UPDATING_PRODUCT);
        }
    }

    public String deleteProduct(String SKU) {
        try {
            var product = findProduct(SKU, productRepository::findBySKU);
            productRepository.delete(product);
            return PRODUCT_DELETED_SUCCESS + " --> " + product;
        } catch (Exception e) {
            throw new ProductValidationException(ERROR_DELETING_PRODUCT + SKU);
        }
    }

    private Product findProduct(String identifier, Function<String, Optional<Product>> finder) {
        return finder.apply(identifier)
                .orElseThrow(() -> new ProductValidationException(PRODUCT_NOT_FOUND + identifier));
    }

    private void updateProductStatus(Product product, boolean status) {
        product.setStatus(status);
    }

    private void updateProductDetails(Product product, ProductRequest request) {
        product.setStock(request.getStock());
        product.setPrice(request.getPrice());
        product.setName(request.getName());
        product.setStatus(request.getStatus());
    }

    private ProductResponse buildProductResponse(Product product, String message) {
        return ProductResponse.builder()
                .message(message)
                .stock(product.getStock())
                .price(product.getPrice())
                .name(product.getName())
                .status(product.isStatus())
                .modificationDate(product.getModificationDate())
                .creationDate(product.getCreationDate())
                .sku(product.getSKU())
                .build();
    }
}