package com.ETicaretDB_restAPI.restAPI.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "UserFavourite")
public class UserFavourite {

    public UserFavourite(){}

    @EmbeddedId
    private UserFavouriteKey id;

    @ManyToOne
    @MapsId("userID")
    @JoinColumn(name = "UserID")
    private User user;

    @ManyToOne
    @MapsId("productID")
    @JoinColumn(name = "ProductID")
    private Product product;

    // GETTER - SETTER
}
