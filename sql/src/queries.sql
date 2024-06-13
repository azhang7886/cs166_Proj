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
 * Get current user's password
 */
SELECT password
FROM Users
WHERE login =  ;

 /*
  * Get current user's role
  */
SELECT role
FROM Users
WHERE login =  ;

/*
 * Update current customer's password
 */
 UPDATE Users
 SET password =  
 WHERE login =  ;


 /*
 * Update current customer's phoneNum
 */
 UPDATE Users
 SET phoneNum =  
 WHERE login =  ;

 /* 
  * View all rental order's 
  * EDIT: only ouput the rentalOrderId in java
  */
 SELECT rentalOrderID, noOfGames, totalPrice, orderTimestamp, dueDate
 FROM RentalOrder
 WHERE login = ;

  /* 
  * View first 5 rental orders 
  * EDIT: only ouput the rentalOrderId in java
  */
 SELECT rentalOrderID, noOfGames, totalPrice, orderTimestamp, dueDate
 FROM RentalOrder
 WHERE login = 
 ORDER BY rentalOrderID
 LIMIT 5;

  /* 
  * View specific rental order ID
  */
 SELECT R.orderTimestamp, R.dueDate, R.totalPrice, T.trackingID
 FROM RentalOrder R, TrackingInfo T, GamesInOrder G
 WHERE R.login =  AND R.rentalOrderID =  AND R.rentalOrderID = G.rentalOrderID AND R.rentalOrderID = T.rentalOrderID;

  /* 
  * View the games for a specific renatal order
  */
 SELECT C.gameName
 FROM RentalOrder R, TrackingInfo T, GamesInOrder G, Catalog C
 WHERE R.login =  AND R.rentalOrderID =  AND R.rentalOrderID = T.rentalOrderID AND R.rentalOrderID = G.rentalOrderID AND G.gameID = C.gameID;

  /* 
  * return the last/highest rental order number
  */
 SELECT rentalOrderID
 FROM RentalOrder
 ORDER BY rentalOrderID DESC
 LIMIT 1;

  /*
  * return price of a game
  */
 SELECT price
 FROM Catalog
 WHERE gameID =  ;

  /*
  * insert rental order into RentalOrder
  */
 INSERT INTO RentalOrder
 VALUES (rentalOrderID, login, noOfGames, totalPrice, orderTimestamp, dueDate);

  /*
  * insert rental order into GamesInOrder
  */
 INSERT INTO GamesInOrder
 VALUES (rentalOrderID, gameID, unitsOrdered); 

  /*
  * update a user's login
  */
 UPDATE Users
 SET login =  
 WHERE login =  ;

 /* 
  * update a user's login for rental Orders 
  */
 UPDATE RentalOrder
 SET login =
 WHERE login =  ;