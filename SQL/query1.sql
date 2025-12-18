SELECT 
    p.Category AS category,
    COUNT(*) AS count
FROM Products p
GROUP BY p.Category
ORDER BY p.Category