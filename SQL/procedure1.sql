CREATE PROCEDURE GetUserFavourites @userId INT
AS
BEGIN
    SELECT * FROM UserFavourite WHERE UserID = @userId;
END;
