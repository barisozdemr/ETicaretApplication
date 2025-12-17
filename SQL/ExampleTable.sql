INSERT INTO Users (UserName, Email, Password) VALUES
('Ali Yýlmaz', 'ali@example.com', 'pass1'),
('Ayþe Demir', 'ayse@example.com', 'pass2'),
('Mehmet Koç', 'mehmet@example.com', 'pass3'),
('Zeynep Kara', 'zeynep@example.com', 'pass4'),
('Umut Baþ', 'umut@example.com', 'pass5');
INSERT INTO Seller (UserID, StoreName, StoreEmail)
VALUES (1, 'TechStore', 'store@example.com');
INSERT INTO Products (SellerID, ProductName, Category, Price, Description, PhotoPath)
VALUES
(1, 'Kulaklýk', 'Elektronik', 799.90, 'Kablosuz kulaklýk, ANC mevcut', 'C:/ETicaretDB_Images/1.jpg'),
(1, 'Mouse', 'Elektronik', 549.50, 'Ergonomik mouse 5000 DPI', 'C:/ETicaretDB_Images/2.jpg'),
(1, 'Klavye', 'Elektronik', 249.00, 'Mekanik klavye', 'C:/ETicaretDB_Images/3.jpg'),
(1, 'Powerbank', 'Elektronik', 399.99, '10000mAh hýzlý þarj', 'C:/ETicaretDB_Images/4.jpg'),
(1, 'Bluetooth Hoparlör', 'Elektronik', 899.00, 'Su geçirmez bluetooth hoparlör', 'C:/ETicaretDB_Images/5.jpg'),
(1, 'Pantolon', 'Giyim', 999.90, 'Kot pantolon renk:rodeo, beden:L', 'C:/ETicaretDB_Images/6.jpg'),
(1, 'Mont', 'Giyim', 1199.00, 'Kýþlýk kalýn mont', 'C:/ETicaretDB_Images/7.jpg');
INSERT INTO UserFavourite (UserID, ProductID) VALUES
(2, 2), (2, 3),
(3, 3), (3, 4),
(4, 4), (4, 5),
(5, 1), (5, 5);
INSERT INTO UserCart (UserID, ProductID) VALUES
(2, 1), (2, 5),
(3, 2), (3, 5),
(4, 1), (4, 3),
(5, 2), (5, 4);
INSERT INTO UserComment (UserID, ProductID, Rating, Comment) VALUES
(2, 2, 3, 'Ortalama'),
(2, 2, 2, 'Beðenmedim'),
(2, 7, 5, 'Harika ürün'),
(3, 3, 4, 'Güzel fakat pahalý'),
(3, 4, 5, 'Tavsiye ederim'),
(4, 6, 3, 'Ýdare eder'),
(4, 5, 5, 'Mükemmel ses'),
(5, 1, 4, 'Gayet yeterli'),
(5, 5, 5, 'Beklediðimden iyi');
