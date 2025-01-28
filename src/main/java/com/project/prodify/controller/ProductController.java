package com.project.prodify.controller;

import com.project.prodify.input.ProductRequest;
import com.project.prodify.output.ProductResponse;
import com.project.prodify.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse  saveProduct (@RequestBody ProductRequest request){
        return productService.saveProduct(request);
    }

    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse findProductName(@PathVariable String name){
        return productService.findProductName(name);
    }
    @GetMapping("/SKU/{SKU}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse findProductSKU(@PathVariable String SKU){
        return productService.findProductSKU(SKU);
    }
    @DeleteMapping("/SKU/{SKU}")
    public String deletedProduct(@PathVariable String SKU){
        return productService.deleteProduct(SKU);
    }

    @PatchMapping("/SKU/{SKU}/{status}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse updateStatusProduct(@PathVariable String SKU, @PathVariable Boolean status){
        return productService.updateStatusProduct(SKU,status);
    }

    @PutMapping("SKU/{SKU}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse updateProduct(@PathVariable String SKU,@RequestBody ProductRequest request){
        return productService.updatProduct(SKU,request);
    }

}
