package com.ETicaretDB_restAPI.restAPI.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Products")
public class Product {

    public Product(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductID")
    private Integer productID;

    @ManyToOne
    @JoinColumn(name = "SellerID", nullable = false)
    private Seller seller;

    @Column(name = "ProductName", nullable = false, length = 200)
    private String productName;

    @Column(name = "Category", length = 100)
    private String category;

    @Column(name = "Price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "Description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "PhotoPath", length = 500)
    private String photoPath;

    // GETTER - SETTER
    public Integer getProductID() { return productID; }
    public void setProductID(Integer productID) { this.productID = productID; }

    public Seller getSeller() { return seller; }
    public void setSeller(Seller seller) { this.seller = seller; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
}
