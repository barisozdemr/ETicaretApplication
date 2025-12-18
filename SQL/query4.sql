DECLARE @productId INT = 4;

SELECT 
    AVG(CAST(Rating AS FLOAT)) AS averageRating,
    COUNT(Rating) AS ratingCount
FROM UserComment
WHERE ProductID = @productId