/* Replace the location to where you saved the data files*/
COPY Users
FROM '/data/class/classes/azhan061/cs166_Proj/data/users.csv'
WITH DELIMITER ',' CSV HEADER;

COPY Catalog
FROM '/data/class/classes/azhan061/cs166_Proj/data/catalog.csv'
WITH DELIMITER ',' CSV HEADER;

COPY RentalOrder
FROM '/data/class/classes/azhan061/cs166_Proj/data/rentalorder.csv'
WITH DELIMITER ',' CSV HEADER;

COPY TrackingInfo
FROM '/data/class/classes/azhan061/cs166_Proj/data/trackinginfo.csv'
WITH DELIMITER ',' CSV HEADER;

COPY GamesInOrder
FROM '/data/class/classes/azhan061/cs166_Proj/data/gamesinorder.csv'
WITH DELIMITER ',' CSV HEADER;
