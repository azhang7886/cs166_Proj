# cs166 Final Project

Authors: Alex Zhang, Peter Lu

Hello! Welcome to our cs166 project: A Game Rental Store

----------------------------------------------------------------------
To access the database please run in the following order
```sh
source startPostgreSQL.sh
source createPostgreSQL.sh
```

To stop the server please run 
```sh
source stopPostgreDB.sh
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
