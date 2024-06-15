# cs166 Final Project

Authors: Alex Zhang, Peter Lu

Game Rental Store is a DBMS project that allows users to rent popular video games.

----------------------------------------------------------------------
To start the server
```sh
cs_166_start
cs_166_status
```

To stop the server please run 
```sh
cs166_db_stop
```
To create the relations
```sh
source sql/scripts/create_db.sh
```
To run our program application
```sh
source java/scripts/compile.sh
```

additional commands:

1. Execute the following command to initialize the PSQL environment.
```sh
cs166_initdb
```
You only need to run this once for the initial setup. Anytime after the initial setup,
when you access the PSQL execution environment, you can just run the commands
below.
2. Execute the following command to start the PostgreSQL server.
```sh
cs166_db_start
```
3. Execute the following command to check the status of the PostgreSQL server.
```sh
cs166_db_status
```
4. Execute the following command to create your database.
```sh
createdb -h localhost -p $PGPORT <netid>_DB
```
5. Check that your database has been created.
```sh
cs166_psql -l
```
6. Once you finish the whole assignment, DO NOT FORGET! to call the following
command to stop the server and shutdown the database.
```sh
cs166_db_stop
```

# Game Rental Store 
database and application constraints

1. Users/Catalog
● New User Registration: when a new user comes to the system, he/she can setup a new
account through your interface, by providing necessary information. The user will
automatically be a customer. Their favorite games will be empty and their
numOverdueGames will be 0.

● User Login/Logout: user can use his/her login and password to login into the system.
Once he/she logs in, a session will be maintained for him/her until logout (an example
has been provided in the Java source code).

● Browse Catalog Games: allows the user to search which games are in the catalog. User
should be able to filter their search based on genre and price (they can search for games
under a certain genre, or search for games under a certain price). User should also be
able to sort the catalog based on price from highest to lowest price and from lowest to
highest price.

● Profile: Users should be able to view and update their favoriteGames list. Users should
also be able to view their numOverdueGames and phoneNum. Users should be able to
change their password & phoneNum. Only managers can edit a user’s login, role, and
numOverdueGames.

● Place Rental Order: user can order any game from the game rental store. User will be
asked to input every gameID and unitsOrdered (the amount of copies of the game they
want) for each game they want to rent. The total price of their rental order should be
returned and output to the user. After placing the rental order, the rental order
information needs to be inserted in the RentalOrder table with a unique rentalOrderID.
Each gameID, rentalOrderID, and the unitsOrdered should be inserted into
GamesInOrder for every game in the order. Also, a TrackingInfo record with a unique
trackingID should be created for the order.

● Update Game Information: For Managers, they can update the information of any game
in the catalog given the gameID.

● Managers: Managers will be able view and update the information of all users (as well
as change their role) and update catalog information.

2. Rental Orders
● See Rental History: Customers will be able to see their order history. They should be
able to see a list of all their past rentalOrderIDs. A customer is not allowed to see the
order history of other customers.

● See Recent 5 Orders: Similar to the last section, customers can see their order
history, but limit the output to the 5 most recent orders.

● Lookup Specific Rental Order: Customers will be able to view details about a
specific rental order. They should be able to see their orderTimestamp, dueDate,
totalPrice, trackingID, and list of games for that order. The due date (return date) of
games can be any future date. A customer is not allowed to see the rental order
information of orders that belong to other customers.

3. Tracking Info
4. 
● View Tracking Information: Customers should be able to view the tracking information
of their rental orders. They should be able to input a trackingID and be provided with
information such as courierName, rentalOrderID, currentLocation, status,
lastUpdatedDate, and additionalComments. A customer is not allowed to see the tracking
information of orders that belong to other customers.

● Update Tracking Info: Employees & managers should be able to update the status,
currentLocation, courierName, and additionalComments of any shipment by providing a
trackingID. Also, the lastUpdateDate should be updated to reflect the fact that the
employee/manager updated the tracking information.

