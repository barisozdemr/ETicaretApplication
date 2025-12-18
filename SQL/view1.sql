CREATE VIEW ProductRatings AS
SELECT ProductID, AVG(CAST(Rating AS FLOAT)) AS AvgRating 
FROM UserComment
GROUP BY ProductID;
