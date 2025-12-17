package com.ETicaretDB_restAPI.restAPI.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "UserComment")
public class UserComment {

    public UserComment(){}

    @EmbeddedId
    private UserCommentKey id;

    @ManyToOne
    @MapsId("userID")
    @JoinColumn(name = "UserID")
    private User user;

    @ManyToOne
    @MapsId("productID")
    @JoinColumn(name = "ProductID")
    private Product product;

    @Column(name = "Rating")
    private Integer rating;

    @Column(name = "Comment", columnDefinition = "NVARCHAR(MAX)")
    private String comment;

    // GETTER - SETTER
    public UserCommentKey getId() { return id; }
    public void setId(UserCommentKey id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
