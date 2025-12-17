package com.ETicaretDB_restAPI.restAPI.repository;

import com.ETicaretDB_restAPI.restAPI.model.entity.Seller;
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
public interface SellerRepository extends JpaRepository<Seller, Integer> {

    @Query(value = "SELECT * FROM Seller WHERE SellerID = :id", nativeQuery = true)
    Optional<Seller> getSellerById(@Param("id") int id);

    // Satıcı ekleme
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Seller (UserID, StoreName, StoreEmail) VALUES (:UserID, :StoreName, :StoreEmail)", nativeQuery = true)
    int addSeller(@Param("UserID") int userId,
                  @Param("StoreName") String storeName,
                  @Param("StoreEmail") String storeEmail);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Seller WHERE SellerID = :id", nativeQuery = true)
    int deleteSellerById(@Param("id") int id);
}

