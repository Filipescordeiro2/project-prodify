package com.project.prodify.service;

import com.project.prodify.domain.Product;
import com.project.prodify.exception.ProductValidationException;
import com.project.prodify.exception.ValidationException;
import com.project.prodify.input.ProductRequest;
import com.project.prodify.output.ProductResponse;
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

    private final ProductRepository productRepository;
    private final Validation validation;

    public ProductResponse saveProduct(ProductRequest request) {
        try {
            validation.validateProductRequest(request);
            var product = new Product(request);
            var productSaved = productRepository.save(product);
            var productResponse = buildProductResponse(productSaved, "Product created successfully");
            log.info("Product created successfully: {}", productResponse);
            return productResponse;
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        } catch (Exception e) {
            log.error("Error creating product", e.getMessage());
            throw new ProductValidationException("Error creating product".concat(e.getMessage()));
        }
    }

    public ProductResponse findProductName(String name) {
        var product = findProduct(name, productRepository::findByName);
        return buildProductResponse(product, "Product found successfully");
    }

    public ProductResponse findProductSKU(String SKU) {
        var product = findProduct(SKU, productRepository::findBySKU);
        return buildProductResponse(product, "Product found successfully");
    }

    public ProductResponse updateStatusProduct(String SKU, Boolean status) {
        try {
            var product = findProduct(SKU, productRepository::findBySKU);
            updateProductStatus(product, status);
            var productSaved = productRepository.save(product);
            var productResponse = buildProductResponse(productSaved, "Product "+product.getSKU()+" with changed status");
            log.info("Product with changed status: {}", productResponse);
            return productResponse;
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        } catch (Exception e) {
            log.error("Error updating product", e);
            throw new ProductValidationException("Error changed status");
        }
    }

    public ProductResponse updatProduct(String SKU,ProductRequest request){
        try {
            var product = findProduct(SKU,productRepository::findBySKU);
            validation.validateProductRequestUpdate(request);
            updateProductDetails(product,request);
            var productSaved = productRepository.save(product);
            var productResponse = buildProductResponse(productSaved, "Product "+product.getSKU()+" updated successfully");
            log.info("Product updated successfully: {}", productResponse);
            return productResponse;
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        } catch (Exception e) {
            log.error("Error updating product", e);
            throw new ProductValidationException("Error updating product");
        }
    }

    public String deleteProduct(String SKU) {
        try {
            var product = findProduct(SKU, productRepository::findBySKU);
            productRepository.delete(product);
            return "Product deleted successfully --> " + product;
        } catch (Exception e) {
            throw new ProductValidationException("Error in deleted for product --> " + SKU);
        }
    }

    private Product findProduct(String identifier, Function<String, Optional<Product>> finder) {
        return finder.apply(identifier)
                .orElseThrow(() -> new ProductValidationException("Product not found with identifier: " + identifier));
    }

    private void updateProductStatus(Product product, boolean status) {
        product.setStock(product.getStock());
        product.setPrice(product.getPrice());
        product.setName(product.getName());
        product.setSKU(product.getSKU());
        product.setCreationDate(product.getCreationDate());
        product.setStatus(status);
    }
    private void updateProductDetails(Product product,ProductRequest request) {
        product.setStock(request.getStock());
        product.setPrice(request.getPrice());
        product.setName(request.getName());
        product.setStatus(request.isStatus());
        product.setSKU(product.getSKU());
        product.setCreationDate(product.getCreationDate());
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
                .SKU(product.getSKU())
                .build();
    }
}