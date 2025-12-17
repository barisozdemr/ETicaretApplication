package com.ETicaretDB_restAPI.restAPI.repository;

import com.ETicaretDB_restAPI.restAPI.model.dto.CategoryCountDTO;
import com.ETicaretDB_restAPI.restAPI.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT * FROM Products", nativeQuery = true)
    List<Product> getAllProducts();

    @Query(value = "SELECT * FROM Products WHERE ProductID = :id", nativeQuery = true)
    Optional<Product> getProductById(@Param("id") int id);

    @Query(value = "SELECT * FROM Products WHERE Category = :category", nativeQuery = true)
    List<Product> getProductByCategory(@Param("category") String category);

    @Query(value = """
SELECT 
    p.Category AS category,
    COUNT(*) AS count
FROM Products p
GROUP BY p.Category
ORDER BY p.Category
""", nativeQuery = true)
    List<CategoryCountDTO> getProductCountByCategory();

    @Modifying
    @Transactional
    @Query(value =
            "INSERT INTO Products(SellerID, ProductName, Category, Price, Description, PhotoPath)" +
            "VALUES (:sellerId, :productName, :category, :price, :description, :photoPath", nativeQuery = true)
    int addProduct(@Param("sellerId") int sellerId,
                   @Param("productName") String productName,
                   @Param("category") String category,
                   @Param("price") BigDecimal price,
                   @Param("description") String description,
                   @Param("photoPath") String photoPath);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Products WHERE ProductID = :id", nativeQuery = true)
    int deleteProductById(@Param("id") int id);
}
