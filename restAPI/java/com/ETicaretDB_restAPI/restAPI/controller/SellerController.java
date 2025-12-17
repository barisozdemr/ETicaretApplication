package com.ETicaretDB_restAPI.restAPI.controller;

import com.ETicaretDB_restAPI.restAPI.model.entity.Seller;
import com.ETicaretDB_restAPI.restAPI.service.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable int id) {
        return ResponseEntity.of(sellerService.getSellerById(id));
    }

    @PostMapping
    public boolean addSeller(@RequestBody Seller seller) {
        return sellerService.addSeller(seller);
    }

    @DeleteMapping("/{id}")
    public boolean deleteSellerById(@PathVariable int id) {
        return sellerService.deleteSellerById(id);
    }
}
