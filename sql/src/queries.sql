/*
 * Create and insert user into the querey
 */
INSERT INTO Users
VALUES (login, password, 'customer', NULL, phoneNum, numOverDueGames);

/*
 * User LogIn using login, password, and phonenumber
 */
SELECT *
FROM Users
WHERE login =  AND password =  AND phoneNum = "";

/*
 * View current customer's profile
 */
SELECT *
FROM Users
WHERE login =  AND password = ;

/*
 * Update current customer's password
 */
 UPDATE Users
 SET password =  
 WHERE login =  ;

/*
 * Get current user's password
 */
SELECT password
FROM Users
WHERE login =  ;

 /*
 * Update current customer's phoneNum
 */
 UPDATE Users
 SET phoneNum =  
 WHERE login =  ;