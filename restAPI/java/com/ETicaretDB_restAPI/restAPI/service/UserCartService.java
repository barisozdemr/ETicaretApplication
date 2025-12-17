package com.ETicaretDB_restAPI.restAPI.service;

import com.ETicaretDB_restAPI.restAPI.model.dto.UserCartDTO;
import com.ETicaretDB_restAPI.restAPI.model.entity.UserCart;
import com.ETicaretDB_restAPI.restAPI.repository.UserCartRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCartService {

    private final UserCartRepository userCartRepository;

    public UserCartService(UserCartRepository repository) {
        this.userCartRepository = repository;
    }

    public List<UserCartDTO> getAllUserCartsByUserId(int userId) {
        return userCartRepository.getAllUserCartsByUserId(userId);
    }

    public Double getTotalCartPriceByUserId(int userId){
        return userCartRepository.getTotalCartPriceByUserId(userId);
    }

    public boolean addUserCart(int userId, int productId) {
        int affected = userCartRepository.addUserCart(userId, productId);

        return affected > 0;
    }

    public boolean deleteUserCartById(int userId, int productId) {
        return 0 < userCartRepository.deleteUserCartById(userId, productId);
    }
}
