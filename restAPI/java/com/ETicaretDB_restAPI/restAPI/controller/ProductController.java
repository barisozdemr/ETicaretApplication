package com.ETicaretDB_restAPI.restAPI.controller;

import com.ETicaretDB_restAPI.restAPI.model.dto.CategoryCountDTO;
import com.ETicaretDB_restAPI.restAPI.model.entity.Product;
import com.ETicaretDB_restAPI.restAPI.service.ProductService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/image/{path}")
    public ResponseEntity<Resource> getProductImage(@PathVariable("path") String path) {
        return productService.getProductImage(path);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") int productId) {
        return ResponseEntity.of(productService.getProductById(productId));
    }

    @GetMapping("/category/{category}")
    public List<Product> getProductByCategory(@PathVariable("category") String category) {
        return productService.getProductByCategory(category);
    }

    @GetMapping("/category/count")
    public List<CategoryCountDTO> getProductCountByCategory(){
        return productService.getProductCountByCategory();
    }

    @PostMapping
    public boolean addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @DeleteMapping("/{id}")
    public boolean deleteProductById(@PathVariable int productId) {
        return productService.deleteProductById(productId);
    }
}
