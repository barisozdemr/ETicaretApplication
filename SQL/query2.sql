DECLARE @productId INT = 5;

SELECT
    uc.UserID,
    u.userName AS userName,
    u.email AS userEmail,
    u.password AS userPassword,

    uc.ProductID,
    p.productName,
    p.category,
    p.price,
    p.description,
    p.photoPath,

    s.sellerID,
    s.storeName,
    s.storeEmail,

    su.userID AS sellerUserID,
    su.userName AS sellerUserName,
    su.email AS sellerUserEmail,
    su.password AS sellerUserPassword,

    uc.rating,
    uc.comment
FROM UserComment uc
JOIN Users u
    ON uc.UserID = u.userID
JOIN Products p
    ON uc.ProductID = p.productID
JOIN Seller s
    ON p.sellerID = s.sellerID
JOIN Users su
    ON s.userID = su.userID
WHERE uc.ProductID = @productId;
