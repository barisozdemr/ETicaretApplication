package com.ETicaretDB_restAPI.restAPI.controller;

import com.ETicaretDB_restAPI.restAPI.model.dto.UserFavouriteDTO;
import com.ETicaretDB_restAPI.restAPI.service.UserFavouriteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favourites")
public class UserFavouriteController {

    private final UserFavouriteService userFavouriteService;

    public UserFavouriteController(UserFavouriteService service) {
        this.userFavouriteService = service;
    }

    @GetMapping("/user/{userId}")
    public List<UserFavouriteDTO> getAllUserFavouritesByUserId(@PathVariable int userId) {
        return userFavouriteService.getAllUserFavouritesByUserId(userId);
    }

    @GetMapping("/product/{productId}")
    public List<UserFavouriteDTO> getAllUserFavouritesByProductId(@PathVariable int productId) {
        return userFavouriteService.getAllUserFavouritesByProductId(productId);
    }

    @PostMapping("/{userId}/{productId}")
    public boolean addUserFavourite(@PathVariable int userId, @PathVariable int productId) {
        return userFavouriteService.addUserFavourite(userId, productId);
    }

    @DeleteMapping("/{userId}/{productId}")
    public boolean deleteUserFavouriteById(@PathVariable int userId, @PathVariable int productId) {
        return userFavouriteService.deleteUserFavouriteById(userId, productId);
    }
}
