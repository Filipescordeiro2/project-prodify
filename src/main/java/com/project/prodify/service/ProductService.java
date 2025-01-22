package com.project.prodify.service;

import com.project.prodify.domain.Product;
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
        } catch (Exception e) {
            log.error("Error creating product", e);
            throw new RuntimeException("Error creating product", e);
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

    public ProductResponse updateProduct(String SKU, ProductRequest request) {
        try {
            validation.validateProductRequest(request);
            var product = findProduct(SKU, productRepository::findBySKU);
            updateProductDetails(product, request);
            var productSaved = productRepository.save(product);
            var productResponse = buildProductResponse(productSaved, "Product updated successfully");
            log.info("Product updated successfully: {}", productResponse);
            return productResponse;
        } catch (Exception e) {
            log.error("Error updating product", e);
            throw new RuntimeException("Error updating product", e);
        }
    }

    public String deleteProduct(String SKU){
        try{
            var prodcut = findProduct(SKU,productRepository::findBySKU);
            productRepository.delete(prodcut);
            return "Product deleted successfully --> "+prodcut;
        }catch (Exception e){
            throw new RuntimeException("Error in deleted for product --> "+SKU);
        }
    }

    private Product findProduct(String identifier, Function<String, Optional<Product>> finder) {
        return finder.apply(identifier)
                .orElseThrow(() -> new RuntimeException("Product not found with identifier: " + identifier));
    }

    private void updateProductDetails(Product product, ProductRequest request) {
        product.setStock(request.getStock());
        product.setPrice(request.getPrice());
        product.setName(request.getName());
        product.setStatus(request.isStatus());
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
