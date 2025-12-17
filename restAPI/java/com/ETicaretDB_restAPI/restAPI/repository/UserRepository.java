package com.ETicaretDB_restAPI.restAPI.repository;

import com.ETicaretDB_restAPI.restAPI.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * FROM Users", nativeQuery = true)
    List<User> getAllUsers();

    @Query(value = "SELECT * FROM Users WHERE UserID = :id", nativeQuery = true)
    User getUserById(@Param("id") int id);

    @Query(value = "SELECT * FROM Users WHERE UserName = :username", nativeQuery = true)
    User getUserByUsername(@Param("username") String username);

    @Modifying
    @Transactional
    @Query(value = """
    UPDATE Users 
    SET 
        UserName = :username,
        Email = :email,
        Password = :password
    WHERE UserID = :id
""", nativeQuery = true)
    int updateUser(@Param("id") int id,
                       @Param("username") String username,
                       @Param("email") String email,
                       @Param("password") String password);

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO Users (UserName, Email, Password)
        VALUES (:username, :email, :password)
        """, nativeQuery = true)
    int addUser(
            @Param("username") String username,
            @Param("email") String email,
            @Param("password") String password
    );

    @Query(value = "SELECT COUNT(*) FROM Users WHERE UserName = :userName", nativeQuery = true)
    int countByUserName(@Param("userName") String userName);

    @Query(value = "SELECT COUNT(*) FROM Users WHERE Email = :email", nativeQuery = true)
    int countByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Users WHERE UserID = :id", nativeQuery = true)
    int deleteUserById(@Param("id") int id);

}
