package com.ETicaretDB_restAPI.restAPI.service;

import com.ETicaretDB_restAPI.restAPI.model.dto.UserFavouriteDTO;
import com.ETicaretDB_restAPI.restAPI.repository.UserFavouriteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFavouriteService {

    private final UserFavouriteRepository userFavouriteRepository;

    public UserFavouriteService(UserFavouriteRepository repository) {
        this.userFavouriteRepository = repository;
    }

    public List<UserFavouriteDTO> getAllUserFavouritesByUserId(int userId) {
        return userFavouriteRepository.getAllUserFavouritesByUserId(userId);
    }

    public List<UserFavouriteDTO> getAllUserFavouritesByProductId(int productId) {
        return userFavouriteRepository.getAllUserFavouritesByProductId(productId);
    }

    public boolean addUserFavourite(int userId, int productId) {
        int affected = userFavouriteRepository.addUserFavourite(userId, productId);

        return affected > 0;
    }

    public boolean deleteUserFavouriteById(int userId, int productId) {
        return 0 < userFavouriteRepository.deleteUserFavouriteById(userId, productId);
    }
}
