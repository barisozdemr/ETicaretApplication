package com.ETicaretDB_restAPI.restAPI.service;

import com.ETicaretDB_restAPI.restAPI.model.dto.CategoryCountDTO;
import com.ETicaretDB_restAPI.restAPI.model.entity.Product;
import com.ETicaretDB_restAPI.restAPI.repository.ProductRepository;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private static final String IMAGE_DIR = "C:/ETicaretDB_Images/";

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts(){
        return productRepository.getAllProducts();
    }

    public ResponseEntity<Resource> getProductImage(String path){
        Path pathX = Paths.get(IMAGE_DIR + path);
        Resource resource = new FileSystemResource(pathX);

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    public Optional<Product> getProductById(int productId){
        return productRepository.getProductById(productId);
    }

    public List<Product> getProductByCategory(String category){
        return productRepository.getProductByCategory(category);
    }

    public List<CategoryCountDTO> getProductCountByCategory(){
        return productRepository.getProductCountByCategory();
    }

    public boolean addProduct(Product product){
        int affected = productRepository.addProduct(
                product.getSeller().getSellerID(),
                product.getProductName(),
                product.getCategory(),
                product.getPrice(),
                product.getDescription(),
                product.getPhotoPath()
        );

        return affected > 0;
    }

    public boolean deleteProductById(int productId) {
        return 0 < productRepository.deleteProductById(productId);
    }
}
