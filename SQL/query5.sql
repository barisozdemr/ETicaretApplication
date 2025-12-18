DECLARE @userId INT = 3;
DECLARE @productId INT = 11;

MERGE UserCart AS target
USING (SELECT @userId AS UserID, @productId AS ProductID) AS source
ON target.UserID = source.UserID AND target.ProductID = source.ProductID
WHEN MATCHED THEN
    UPDATE SET Quantity = Quantity + 1
WHEN NOT MATCHED THEN
    INSERT (UserID, ProductID, Quantity)
    VALUES (@userId, @productId, 1);