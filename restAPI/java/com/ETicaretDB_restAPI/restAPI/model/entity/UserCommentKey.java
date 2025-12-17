package com.ETicaretDB_restAPI.restAPI.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserCommentKey implements Serializable {

    @Column(name = "UserID")
    private Integer userID;

    @Column(name = "ProductID")
    private Integer productID;

    public UserCommentKey() {}

    public UserCommentKey(Integer userID, Integer productID) {
        this.userID = userID;
        this.productID = productID;
    }

    // equals & hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserCommentKey)) return false;
        UserCommentKey that = (UserCommentKey) o;
        return Objects.equals(userID, that.userID) &&
                Objects.equals(productID, that.productID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, productID);
    }
}
