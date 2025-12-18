DECLARE @userId INT = 3;

SELECT 
    SUM(p.Price)
FROM UserCart uc
JOIN Products p ON uc.ProductID = p.ProductID
WHERE uc.UserID = @userId