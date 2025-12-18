CREATE TABLE Users (
    UserID INT IDENTITY(1,1) PRIMARY KEY,
    UserName NVARCHAR(100) NOT NULL,
    Email NVARCHAR(200) NOT NULL UNIQUE,
    Password NVARCHAR(200) NOT NULL
);
CREATE TABLE Seller (
    SellerID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    StoreName NVARCHAR(200) NOT NULL,
    StoreEmail NVARCHAR(200) NOT NULL,

    CONSTRAINT FK_Seller_Users
        FOREIGN KEY (UserID) REFERENCES Users(UserID)
        ON DELETE CASCADE
);
CREATE TABLE Products (
    ProductID INT IDENTITY(1,1) PRIMARY KEY,
    SellerID INT NOT NULL,
    ProductName NVARCHAR(200) NOT NULL,
    Category NVARCHAR(100),
    Price DECIMAL(10,2) NOT NULL,
    Description NVARCHAR(MAX),
    PhotoPath NVARCHAR(500),

    CONSTRAINT FK_Products_Seller
        FOREIGN KEY (SellerID) REFERENCES Seller(SellerID)
        ON DELETE NO ACTION
);
CREATE TABLE UserComment (
    UserID INT NOT NULL,
    ProductID INT NOT NULL,
    Rating INT,
    Comment NVARCHAR(MAX),

    CONSTRAINT PK_UserComment PRIMARY KEY (UserID, ProductID),

    CONSTRAINT FK_UserComment_Users
        FOREIGN KEY (UserID) REFERENCES Users(UserID)
        ON DELETE CASCADE,

    CONSTRAINT FK_UserComment_Products
        FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
        ON DELETE CASCADE
);
CREATE TABLE UserFavourite (
    UserID INT NOT NULL,
    ProductID INT NOT NULL,

    CONSTRAINT PK_UserFavourite PRIMARY KEY (UserID, ProductID),

    CONSTRAINT FK_UserFavourite_Users
        FOREIGN KEY (UserID) REFERENCES Users(UserID)
        ON DELETE CASCADE,

    CONSTRAINT FK_UserFavourite_Products
        FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
        ON DELETE CASCADE
);
CREATE TABLE UserCart (
    UserID INT NOT NULL,
    ProductID INT NOT NULL,

    CONSTRAINT PK_UserCart PRIMARY KEY (UserID, ProductID),

    CONSTRAINT FK_UserCart_Users
        FOREIGN KEY (UserID) REFERENCES Users(UserID)
        ON DELETE CASCADE,

    CONSTRAINT FK_UserCart_Products
        FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
        ON DELETE CASCADE
);