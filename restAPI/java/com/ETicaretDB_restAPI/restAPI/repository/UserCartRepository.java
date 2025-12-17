package com.ETicaretDB_restAPI.restAPI.repository;

import com.ETicaretDB_restAPI.restAPI.model.dto.UserCartDTO;
import com.ETicaretDB_restAPI.restAPI.model.entity.UserCart;
import com.ETicaretDB_restAPI.restAPI.model.entity.UserCartKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserCartRepository extends JpaRepository<UserCart, UserCartKey> {

    @Query(value = """
SELECT 
    uc.UserID AS userId,
    uc.ProductID AS productId,
    uc.Quantity AS quantity
FROM UserCart uc
WHERE uc.UserID = :userId
""", nativeQuery = true)
    List<UserCartDTO> getAllUserCartsByUserId(@Param("userId") int userId);

    @Query(value = """
SELECT 
    SUM(p.Price)
FROM UserCart uc
JOIN Products p ON uc.ProductID = p.ProductID
WHERE uc.UserID = :userId
""", nativeQuery = true)
    Double getTotalCartPriceByUserId(@Param("userId") int userId);

    @Modifying
    @Transactional
    @Query(value = """
MERGE UserCart AS target
USING (SELECT :userId AS UserID, :productId AS ProductID) AS source
ON target.UserID = source.UserID AND target.ProductID = source.ProductID
WHEN MATCHED THEN
    UPDATE SET Quantity = Quantity + 1
WHEN NOT MATCHED THEN
    INSERT (UserID, ProductID, Quantity)
    VALUES (:userId, :productId, 1);
""", nativeQuery = true)
    int addUserCart(@Param("userId") int userId,
                   @Param("productId") int productId);

    // Yorum sil
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM UserCart WHERE UserID = :userId AND ProductID = :productId", nativeQuery = true)
    int deleteUserCartById(@Param("userId") int userId, @Param("productId") int productId);
}
