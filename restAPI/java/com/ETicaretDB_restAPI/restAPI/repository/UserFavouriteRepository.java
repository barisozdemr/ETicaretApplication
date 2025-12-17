package com.ETicaretDB_restAPI.restAPI.repository;

import com.ETicaretDB_restAPI.restAPI.model.dto.UserFavouriteDTO;
import com.ETicaretDB_restAPI.restAPI.model.entity.UserFavourite;
import com.ETicaretDB_restAPI.restAPI.model.entity.UserFavouriteKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserFavouriteRepository extends JpaRepository<UserFavourite, UserFavouriteKey> {

    @Query(value = """
SELECT 
    uc.UserID AS userId,
    uc.ProductID AS productId
FROM UserFavourite uc
WHERE uc.UserID = :userId
""", nativeQuery = true)
    List<UserFavouriteDTO> getAllUserFavouritesByUserId(@Param("userId") int userId);

    @Query(value = """
SELECT 
    uc.UserID AS userId,
    uc.ProductID AS productId
FROM UserFavourite uc
WHERE uc.ProductID = :productId
""", nativeQuery = true)
    List<UserFavouriteDTO> getAllUserFavouritesByProductId(@Param("productId") int productId);

    // Yorum ekle
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO UserFavourite (UserID, ProductID) " +
            "VALUES (:userId, :productId)", nativeQuery = true)
    int addUserFavourite(@Param("userId") int userId,
                    @Param("productId") int productId);

    // Yorum sil
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM UserFavourite WHERE UserID = :userId AND ProductID = :productId", nativeQuery = true)
    int deleteUserFavouriteById(@Param("userId") int userId, @Param("productId") int productId);
}
