package com.ETicaretDB_restAPI.restAPI.repository;

import com.ETicaretDB_restAPI.restAPI.model.dto.RatingDTO;
import com.ETicaretDB_restAPI.restAPI.model.entity.UserComment;
import com.ETicaretDB_restAPI.restAPI.model.entity.UserCommentKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCommentRepository extends JpaRepository<UserComment, UserCommentKey> {

    @Query(value = "SELECT * FROM UserComment WHERE UserID = :userId AND ProductID = :productId", nativeQuery = true)
    UserComment getCommentById(@Param("userId") int userId, @Param("productId") int productId);

    @Query(value =
            "SELECT \n" +
            "    uc.UserID,\n" +
            "    u.userName AS userName,\n" +
            "    u.email AS userEmail,\n" +
            "    u.password AS userPassword,\n" +
            "\n" +
            "    uc.ProductID,\n" +
            "    p.productName,\n" +
            "    p.category,\n" +
            "    p.price,\n" +
            "    p.description,\n" +
            "    p.photoPath,\n" +
            "\n" +
            "    s.sellerID,\n" +
            "    s.storeName,\n" +
            "    s.storeEmail,\n" +
            "\n" +
            "    su.userID AS sellerUserID,\n" +
            "    su.userName AS sellerUserName,\n" +
            "    su.email AS sellerUserEmail,\n" +
            "    su.password AS sellerUserPassword,\n" +
            "\n" +
            "    uc.rating,\n" +
            "    uc.comment\n" +
            "FROM UserComment uc\n" +
            "JOIN Users u \n" +
            "    ON uc.UserID = u.userID\n" +
            "JOIN Products p \n" +
            "    ON uc.ProductID = p.productID\n" +
            "JOIN Seller s \n" +
            "    ON p.sellerID = s.sellerID\n" +
            "JOIN Users su \n" +
            "    ON s.userID = su.userID\n" +
            "WHERE uc.ProductID = :productId;\n", nativeQuery = true)
    List<UserComment> getAllCommentsByProductId(@Param("productId") int productId);


    @Query(
            value = """
        SELECT 
            AVG(CAST(Rating AS FLOAT)) AS averageRating,
            COUNT(Rating) AS ratingCount
        FROM UserComment
        WHERE ProductID = :productId
        """,
            nativeQuery = true
    )
    RatingDTO getRatingByProductId(@Param("productId") int productId);

    // Yorum ekle
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO UserComment (UserID, ProductID, Rating, Comment) " +
            "VALUES (:userId, :productId, :rating, :comment)", nativeQuery = true)
    int addComment(@Param("userId") int userId,
                   @Param("productId") int productId,
                   @Param("rating") Integer rating,
                   @Param("comment") String comment);

    // Yorum sil
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM UserComment WHERE UserID = :userId AND ProductID = :productId", nativeQuery = true)
    int deleteCommentById(@Param("userId") int userId, @Param("productId") int productId);

}
