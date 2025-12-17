package com.ETicaretDB_restAPI.restAPI.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Seller")
public class Seller {

    public Seller(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SellerID")
    private Integer sellerID;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @Column(name = "StoreName", nullable = false, length = 200)
    private String storeName;

    @Column(name = "StoreEmail", nullable = false, length = 200)
    private String storeEmail;

    // GETTER - SETTER
    public Integer getSellerID() { return sellerID; }
    public void setSellerID(Integer sellerID) { this.sellerID = sellerID; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getStoreEmail() { return storeEmail; }
    public void setStoreEmail(String storeEmail) { this.storeEmail = storeEmail; }
}
