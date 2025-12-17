package com.ETicaretDB_restAPI.restAPI.controller;

import com.ETicaretDB_restAPI.restAPI.model.dto.UserCartDTO;
import com.ETicaretDB_restAPI.restAPI.model.entity.UserCart;
import com.ETicaretDB_restAPI.restAPI.service.UserCartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class UserCartController {

    private final UserCartService userCartService;

    public UserCartController(UserCartService service) {
        this.userCartService = service;
    }

    @GetMapping("/{userId}")
    public List<UserCartDTO> getAllUserCartsByUserId(@PathVariable int userId) {
        return userCartService.getAllUserCartsByUserId(userId);
    }

    @GetMapping("/total/{userId}")
    public Double getTotalCartPriceByUserId(@PathVariable int userId){
        return userCartService.getTotalCartPriceByUserId(userId);
    }

    @PostMapping("/{userId}/{productId}")
    public boolean addUserCart(@PathVariable int userId, @PathVariable int productId) {
        return userCartService.addUserCart(userId, productId);
    }

    @DeleteMapping("/{userId}/{productId}")
    public boolean deleteUserCartById(@PathVariable int userId, @PathVariable int productId) {
        return userCartService.deleteUserCartById(userId, productId);
    }
}
