package com.project.prodify.service;

import com.project.prodify.domain.Product;
import com.project.prodify.input.ProductRequest;
import com.project.prodify.output.ProductResponse;
import com.project.prodify.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse saveProduct(ProductRequest request) {
        var product = new Product(request);
        var productSaved = productRepository.save(product);
        var productReponse = ProductResponse
                .builder()
                .message("Product created susuccessfully")
                .stock(productSaved.getStock())
                .price(productSaved.getPrice())
                .name(productSaved.getName())
                .status(productSaved.isStatus())
                .modificationDate(productSaved.getModificationDate())
                .creationDate(productSaved.getCreationDate())
                .build();
        return productReponse;
    }

}
