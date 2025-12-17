package com.ETicaretDB_restAPI.restAPI.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "UserCart")
public class UserCart {

    @EmbeddedId
    private UserCartKey id;

    @ManyToOne
    @MapsId("userID")
    @JoinColumn(name = "UserID")
    private User user;

    @ManyToOne
    @MapsId("productID")
    @JoinColumn(name = "ProductID")
    private Product product;

    @Column(name = "Quantity", nullable = false)
    private int quantity;

    // GETTER - SETTER
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
