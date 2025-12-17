package com.ETicaretDB_restAPI.restAPI.service;

import com.ETicaretDB_restAPI.restAPI.model.entity.Seller;
import com.ETicaretDB_restAPI.restAPI.repository.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public Optional<Seller> getSellerById(int sellerId) {
        return sellerRepository.getSellerById(sellerId);
    }

    public boolean addSeller(Seller seller) {
        // 1) Native insert çağır
        int affected = sellerRepository.addSeller(
                seller.getUser().getUserID(),
                seller.getStoreName(),
                seller.getStoreEmail()
        );

        return affected > 0;
    }

    public boolean deleteSellerById(int sellerId) {
        return 0 < sellerRepository.deleteSellerById(sellerId);
    }
}

