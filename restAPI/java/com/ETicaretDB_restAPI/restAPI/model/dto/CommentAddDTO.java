package com.ETicaretDB_restAPI.restAPI.model.dto;

public class CommentAddDTO {
    private int userId;
    private int productId;
    private int rating;
    private String comment;

    public int getUserId(){
        return this.userId;
    }
    public int getProductId(){
        return this.productId;
    }
    public int getRating(){
        return this.rating;
    }
    public String getComment(){
        return this.comment;
    }
}