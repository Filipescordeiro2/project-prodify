package com.project.prodify.controller;

import com.project.prodify.dto.request.ProductRequest;
import com.project.prodify.dto.response.ProductResponse;
import com.project.prodify.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductRequest request) {
        return productService.saveProduct(request);
    }

    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductByName(@PathVariable String name) {
        return productService.findProductName(name);
    }

    @GetMapping("/SKU/{SKU}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductBySKU(@PathVariable String SKU) {
        return productService.findProductSKU(SKU);
    }

    @DeleteMapping("/SKU/{SKU}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String SKU) {
        productService.deleteProduct(SKU);
    }

    @PatchMapping("/SKU/{SKU}/status/{status}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse updateProductStatus(@PathVariable String SKU, @PathVariable Boolean status) {
        return productService.updateStatusProduct(SKU, status);
    }

    @PutMapping("/SKU/{SKU}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse updateProduct(@PathVariable String SKU, @RequestBody ProductRequest request) {
        return productService.updateProduct(SKU, request);
    }
}