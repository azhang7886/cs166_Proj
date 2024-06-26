/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */

//JDBC is installed!
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Calendar;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class GameRental {

  // reference to physical database connection.
  private Connection _connection = null;

  // handling the keyboard inputs through a BufferedReader
  // This variable can be global for convenience.
  static BufferedReader in = new BufferedReader(
    new InputStreamReader(System.in));

  /**
   * Creates a new instance of GameRental store
   *
   * @param hostname the MySQL or PostgreSQL server hostname
   * @param database the name of the database
   * @param username the user name used to login to the database
   * @param password the user login password
   * @throws java.sql.SQLException when failed to make a connection.
   */
  public GameRental(String dbname, String dbport, String user, String passwd) throws SQLException {
    System.out.println("=======================================================");
    System.out.println("|                           o                         |");
    System.out.println("|                           o                         |");
    System.out.println("|                           o                         |");
    System.out.println("|                 Connecting to database              |");
    System.out.println("|                           o                         |");
    System.out.println("|                           o                         |");
    System.out.println("|                           o                         |");

    try {
      // constructs the connection URL
      String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
      //System.out.println ("Connection URL: " + url + "\n");

      // obtain a physical connection
      this._connection = DriverManager.getConnection(url, user, passwd);
      // System.out.println("|                o               |");
      System.out.println("|            Database Connection Successful!          |");
      System.out.println("|                    Loading Main Menu                |");
      System.out.println("|                           o                         |");
      System.out.println("|                           o                         |");
      System.out.println("|                           o                         |");
      System.out.println("=======================================================");
      System.out.println("|                       Main Menu                     |");
      System.out.println("=======================================================");
    } catch (Exception e) {
      System.err.println("Error - Unable to Connect to Database: " + e.getMessage());
      System.out.println("Make sure you started postgres on this machine");
      System.exit(-1);
    } //end catch
  } //end GameRental

  /**
   * Method to execute an update SQL statement.  Update SQL instructions
   * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
   *
   * @param sql the input SQL string
   * @throws java.sql.SQLException when update failed
   */
  public void executeUpdate(String sql) throws SQLException {
    // creates a statement object
    Statement stmt = this._connection.createStatement();

    // issues the update instruction
    stmt.executeUpdate(sql);

    // close the instruction
    stmt.close();
  } //end executeUpdate

  /**
   * Method to execute an input query SQL instruction (i.e. SELECT).  This
   * method issues the query to the DBMS and outputs the results to
   * standard out.
   *
   * @param query the input query string
   * @return the number of rows returned
   * @throws java.sql.SQLException when failed to execute the query
   */
  public int executeQueryAndPrintResult(String query) throws SQLException {
    // creates a statement object
    Statement stmt = this._connection.createStatement();

    // issues the query instruction
    ResultSet rs = stmt.executeQuery(query);

    /*
     ** obtains the metadata object for the returned result set.  The metadata
     ** contains row and column info.
     */
    ResultSetMetaData rsmd = rs.getMetaData();
    int numCol = rsmd.getColumnCount();
    int rowCount = 0;

    // iterates through the result set and output them to standard out.
    boolean outputHeader = true;
    while (rs.next()) {
      if (outputHeader) {
        for (int i = 1; i <= numCol; i++) {
          System.out.print(rsmd.getColumnName(i) + "\t");
        }
        System.out.println();
        outputHeader = false;
      }
      for (int i = 1; i <= numCol; ++i)
        System.out.print(rs.getString(i) + "\t");
      System.out.println();
      ++rowCount;
    } //end while
    stmt.close();
    return rowCount;
  } //end executeQuery

  /**
   * Method to execute an input query SQL instruction (i.e. SELECT).  This
   * method issues the query to the DBMS and returns the results as
   * a list of records. Each record in turn is a list of attribute values
   *
   * @param query the input query string
   * @return the query result as a list of records
   * @throws java.sql.SQLException when failed to execute the query
   */
  public List < List < String >> executeQueryAndReturnResult(String query) throws SQLException {
    // creates a statement object
    Statement stmt = this._connection.createStatement();

    // issues the query instruction
    ResultSet rs = stmt.executeQuery(query);

    /*
     ** obtains the metadata object for the returned result set.  The metadata
     ** contains row and column info.
     */
    ResultSetMetaData rsmd = rs.getMetaData();
    int numCol = rsmd.getColumnCount();
    int rowCount = 0;

    // iterates through the result set and saves the data returned by the query.
    boolean outputHeader = false;
    List < List < String >> result = new ArrayList < List < String >> ();
    while (rs.next()) {
      List < String > record = new ArrayList < String > ();
      for (int i = 1; i <= numCol; ++i)
        record.add(rs.getString(i));
      result.add(record);
    } //end while
    stmt.close();
    return result;
  } //end executeQueryAndReturnResult

  /**
   * Method to execute an input query SQL instruction (i.e. SELECT).  This
   * method issues the query to the DBMS and returns the number of results
   *
   * @param query the input query string
   * @return the number of rows returned
   * @throws java.sql.SQLException when failed to execute the query
   */
  public int executeQuery(String query) throws SQLException {
    // creates a statement object
    Statement stmt = this._connection.createStatement();

    // issues the query instruction
    ResultSet rs = stmt.executeQuery(query);

    int rowCount = 0;

    // iterates through the result set and count nuber of results.
    while (rs.next()) {
      rowCount++;
    } //end while
    stmt.close();
    return rowCount;
  }

  /**
   * Method to fetch the last value from sequence. This
   * method issues the query to the DBMS and returns the current
   * value of sequence used for autogenerated keys
   *
   * @param sequence name of the DB sequence
   * @return current value of a sequence
   * @throws java.sql.SQLException when failed to execute the query
   */
  public int getCurrSeqVal(String sequence) throws SQLException {
    Statement stmt = this._connection.createStatement();

    ResultSet rs = stmt.executeQuery(String.format("Select currval('%s')", sequence));
    if (rs.next())
      return rs.getInt(1);
    return -1;
  }

  /**
   * Method to close the physical connection if it is open.
   */
  public void cleanup() {
    try {
      if (this._connection != null) {
        this._connection.close();
      } //end if
    } catch (SQLException e) {
      // ignored.
    } //end try
  } //end cleanup

  /**
   * The main execution method
   *
   * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
   */
  public static void main(String[] args) {
    if (args.length != 3) {
      System.err.println(
        "Usage: " +
        "java [-classpath <classpath>] " +
        GameRental.class.getName() +
        " <dbname> <port> <user>");
      return;
    } //end if

    Greeting();
    GameRental esql = null;
    try {
      // use postgres JDBC driver.
      Class.forName("org.postgresql.Driver").newInstance();
      // instantiate the GameRental object and creates a physical
      // connection.
      String dbname = args[0];
      String dbport = args[1];
      String user = args[2];
      esql = new GameRental(dbname, dbport, user, "");

      boolean keepon = true;
      while (keepon) {
        // These are sample SQL statements
        System.out.println("=======================================================");
        System.out.println("=======================================================");
        System.out.println("|            Type 1 to create an account.             |");
        System.out.println("|                                                     |");
        System.out.println("|            Type 2 to login to account.              |");
        System.out.println("|                                                     |");
        System.out.println("|            Type 9 to exit the app.                  |");
        System.out.println("=======================================================");
        // System.out.println("1. Create an account!");
        // System.out.println("2. Log in");
        // System.out.println("9. < EXIT");
        String authorisedUser = null;
        switch (readChoice()) {
        case 1:
          CreateUser(esql);
          break;
        case 2:
          authorisedUser = LogIn(esql);
          break;
        case 9:
          keepon = false;
          break;
        default:
          System.out.println("|   Invalid selection! try again     |");
          break;
        } //end switch
        if (authorisedUser != null) {
          System.out.println("|                           o                         |");
          System.out.println("|                           o                         |");
          System.out.println("|                           o                         |");
          System.out.println("|                    Successful Login!                |");
          System.out.println("|                    Loading Homepage                 |");
          System.out.println("|                           o                         |");
          System.out.println("|                           o                         |");
          System.out.println("|                           o                         |");
          boolean usermenu = true;
          while (usermenu) {
            System.out.println("|                           o                         |");
            System.out.println("|                           o                         |");
            System.out.println("|                           o                         |");
            System.out.println("|-----------------------------------------------------|");
            System.out.println("|                        MY HOME                      |");
            System.out.println("|-----------------------------------------------------|");
            System.out.println("|                                                     |");
            System.out.println("|                                                     |");
            System.out.println("|         1.         My Profile                       |");
            System.out.println("|                                                     |");
            System.out.println("|         2.      Update My Profile                   |");
            System.out.println("|                                                     |");
            System.out.println("|         3.        View Catalog                      |");
            System.out.println("|                                                     |");
            System.out.println("|         4.     Place Rental Order                   |");
            System.out.println("|                                                     |");
            System.out.println("|         5. View Full Rental Order History           |");
            System.out.println("|                                                     |");
            System.out.println("|         6. View Past 5 Rental Orders                |");
            System.out.println("|                                                     |");
            System.out.println("|         7. View Rental Order Information            |");
            System.out.println("|                                                     |");
            System.out.println("|         8. View Tracking Information                |");
            System.out.println("|                                                     |");
            System.out.println("|                                                     |");

            //the following functionalities basically used by employees & managers
            System.out.println("|-----------------------------------------------------|");
            System.out.println("|              Employee and Managers Access*          |");
            System.out.println("|-----------------------------------------------------|");
            System.out.println("|                                                     |");
            System.out.println("|           9. Update Tracking Information            |");
            System.out.println("|                                                     |");
            System.out.println("|                                                     |");

            //the following functionalities basically used by managers
            System.out.println("|-----------------------------------------------------|");
            System.out.println("|                   Managers Access*                  |");
            System.out.println("|-----------------------------------------------------|");
            System.out.println("|                                                     |");
            System.out.println("|                  10. Update Catalog                 |");
            System.out.println("|                  11. Update User                    |");
            System.out.println("|                                                     |");
            System.out.println("|-----------------------------------------------------|");
            System.out.println("|-----------------------------------------------------|");
            System.out.println("|                                                     |");
            System.out.println("|                    20. Log out                      |");
            System.out.println("|                                                     |");
            System.out.println("|-----------------------------------------------------|");

            switch (readChoice()) {
            case 1:

              viewProfile(esql, authorisedUser);
              break;

            case 2:

              updateProfile(esql, authorisedUser);
              break;

            case 3:

              viewCatalog(esql, authorisedUser);
              break;

            case 4:

              placeOrder(esql, authorisedUser);
              break;

            case 5:

              viewAllOrders(esql, authorisedUser);
              break;

            case 6:

              viewRecentOrders(esql, authorisedUser);
              break;

            case 7:

              viewOrderInfo(esql, authorisedUser);
              break;

            case 8:

              viewTrackingInfo(esql, authorisedUser);
              break;

            case 9:

              updateTrackingInfo(esql, authorisedUser);
              break;

            case 10:

              updateCatalog(esql, authorisedUser);
              break;

            case 11:

              updateUser(esql, authorisedUser);
              break;

            case 20:

              usermenu = false;
              break;

            default:
              System.out.println("|    Invalid selection! try again   |");
              break;

            }
          }
        }
      } //end while
    } catch (Exception e) {
      System.err.println(e.getMessage());
    } finally {
      // make sure to cleanup the created table and close the connection.
      try {
        if (esql != null) {
          System.out.println("|-----------------------------------------------------|");
          System.out.println("|-----------------------------------------------------|");
          System.out.println("|                           o                         |");
          System.out.println("|                           o                         |");
          System.out.println("|                           o                         |");
          System.out.println("|              Disconnecting from database            |");
          System.out.println("|                           o                         |");
          System.out.println("|                           o                         |");
          System.out.println("|                           o                         |");
          esql.cleanup();
          System.out.println("|                          Bye!                       |");
          System.out.println("|-----------------------------------------------------|");
          System.out.println("|-----------------------------------------------------|");
        } //end if
      } catch (Exception e) {
        // ignored.
      } //end try
    } //end try
  } //end main

  public static void Greeting() {
    System.out.println("*******************************************************");
    System.out.println("*******************************************************");
    System.out.println("|                                                     |");
    System.out.println("|                  Game Rental Store                  |");
    System.out.println("|                                                     |");
    System.out.println("*******************************************************");
    System.out.println("|                                                     |");
    System.out.println("|        Welcome to our Game Rental Store App         |");
    System.out.println("|              By Alex Zhang and Peter Lu             |");
    System.out.println("|                                                     |");
    System.out.println("*******************************************************");
  } //end Greeting  

  /*
   * Reads the users choice given from the keyboard
   * @int
   **/
  public static int readChoice() {
    int input;
    // returns only if a correct value is given.
    do {
      System.out.println("|                           o                         |");
      System.out.println("|               Please make your choice:              |");
      System.out.println("|                           o                         |");
      try { // read the integer, parse it and break. 
        input = Integer.parseInt(in.readLine());
        break;
      } catch (Exception e) {
        System.out.println("|    Invalid selection! try again   |");
        continue;
      } //end try
    } while (true);
    return input;
  } //end readChoice

  /*
   * Creates a new user
   */
  public static void CreateUser(GameRental esql) {
    try {
      System.out.println("*******************************************************");
      System.out.println("=======================================================");
      System.out.println("|                     Create User                     |");
      System.out.println("=======================================================");
      System.out.println("|                                                     |");
      System.out.println("|                                                     |");

      System.out.print("|    Enter login name: ");
      String login = in.readLine();
      System.out.print("   |");
      System.out.print("|    Enter password: ");
      String password = in.readLine();
      System.out.print("   |");

      System.out.print("|    Enter phone number (just the 10 digits): ");
      String phoneNum = in.readLine();
      System.out.print("   |");

      String query = "INSERT INTO Users(login, password, role, phoneNum) VALUES ('" + login + "', '" + password + "', 'customer', '" + phoneNum + "');";

      esql.executeUpdate(query);
      System.out.println("|                           o                         |");
      System.out.println("|                           o                         |");
      System.out.println("|                           o                         |");
      System.out.println("|                    Creating account                 |");
      System.out.println("|                           o                         |");
      System.out.println("|                           o                         |");
      System.out.println("|                           o                         |");
      System.out.println("|                User created successfully!           |");
      System.out.println("|                                                     |");
      System.out.println("=======================================================");
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  /*
   * Check log in credentials for an existing user
   * @return User login or null is the user does not exist
   **/
  public static String LogIn(GameRental esql) {
    try {
      System.out.print("|   Enter login name: ");
      String login = in.readLine();
      System.out.println("   |");
      System.out.print("|   Enter password: ");
      String password = in.readLine();
      System.out.println("   |");
      System.out.print("|   Enter phone number (just the 10 digits): ");
      String phoneNum = in.readLine();
      System.out.println("   |");
      String query = "SELECT * FROM Users WHERE login = '" + login + "' AND password =  '" + password + "' AND phoneNum = '" + phoneNum + "';";

      int userExists = esql.executeQuery(query);
      if (userExists > 0) {
        return login;
      } else {
        System.out.println("User does not exist");
        return null;
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    return null;
  } //end

  // Rest of the functions definition go in here

  public static void viewProfile(GameRental esql, String authorisedUser) {
    try {
      String login = authorisedUser;
      String query = "SELECT * FROM Users WHERE login = '" + login + "';";

      // Execute the query and get the result
      List < List < String >> result = esql.executeQueryAndReturnResult(query);

      // Check if the result is empty
      if (result.isEmpty()) {
        System.out.println("No user found with login: " + login);
        return;
      }

      // Assuming the Users table has columns in the order: login, password, role, favGames, phoneNum, numOverDueGames
      List < String > user = result.get(0);
      System.out.println("|                           o                         |");
      System.out.println("|                           o                         |");
      System.out.println("|                           o                         |");
      System.out.println("|                    Loading Profile                  |");
      System.out.println("|                           o                         |");
      System.out.println("|                           o                         |");
      System.out.println("|                           o                         |");
      System.out.println("=======================================================");
      System.out.println("|                       My Profile                    |");
      System.out.println("=======================================================");
      System.out.println("| Login: " + user.get(0));
      System.out.println("| Password: " + user.get(1));
      System.out.println("| Role: " + user.get(2));
      System.out.println("| Favorite Games: " + user.get(3));
      System.out.println("| Phone Number: " + user.get(4));
      System.out.println("| Number of Overdue Games: " + user.get(5));
      System.out.println("====================================");
      System.out.println("| Press any key to return to the homepage...");
      in.readLine(); // Wait for the user to press Enter or any key

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  public static void updateProfile(GameRental esql, String authorisedUser) {
    try {
      System.out.println("|                           o                         |");
      System.out.println("|                           o                         |");
      System.out.println("|                           o                         |");
      System.out.println("======================================================");
      System.out.println("|                  Update Your Profile                |");
      System.out.println("======================================================");
      System.out.println("|                                                     |");
      System.out.println("| 1.                Update password                   |");
      System.out.println("|                                                     |");
      System.out.println("| 2.               Update phone number                |");
      System.out.println("|                                                     |");
      System.out.println("| 3.              Update favorite games               |");
      System.out.println("|                                                     |");
      System.out.println("|-----------------------------------------------------|");
      switch (readChoice()) {
      case 1:
        updatePassword(esql, authorisedUser);
        break;
      case 2:
        updatePhoneNum(esql, authorisedUser);
        break;
      case 3:
        updateFavGame(esql, authorisedUser);
        break;
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public static String getPass(GameRental esql, String authorisedUser) {
    try {
      String query = "SELECT password FROM Users WHERE login = '" + authorisedUser + "';";
      int correct = esql.executeQueryAndPrintResult(query);
      List < List < String >> currPass = esql.executeQueryAndReturnResult(query);
      String correctPass = currPass.get(0).get(0);
      return correctPass;
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    return null;
  }

  public static String getRole(GameRental esql, String authorisedUser) {
    try {
      String query = "SELECT password FROM Users WHERE login = '" + authorisedUser + "';";
      int correct = esql.executeQueryAndPrintResult(query);
      List < List < String >> currRole = esql.executeQueryAndReturnResult(query);
      String returnRole = currRole.get(0).get(0);
      return returnRole;
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    return null;
  }

  public static void updatePassword(GameRental esql, String authorisedUser) {
    try {
      System.out.println("=======================================================");
      System.out.println("|                           o                         |");
      System.out.println("|                           o                         |");
      System.out.println("|                           o                         |");
      System.out.println("|                 Enter current password:              ");
      String password = in.readLine().trim();
      System.out.println("|");
      String correctPassword = getPass(esql, authorisedUser);
      correctPassword.trim();
      while (!password.equals(correctPassword)) {
        System.out.println("====================================================");
        System.out.println("|                                                  |");
        System.out.println("|                   Wrong password                 |");
        System.out.println("|                 1. reenter password              |");
        System.out.println("|                     2. Go back                   |");
        switch (readChoice()) {
        case 1:
          System.out.println("|                                            |");
          System.out.println("              reenter password:               ");
          password = in.readLine();
          break;
        case 2:
          System.out.println("|                           o                         |");
          System.out.println("|                           o                         |");
          System.out.println("|                           o                         |");
          System.out.println("|                       Going back                    |");
          System.out.println("|                           o                         |");
          System.out.println("|                           o                         |");
          System.out.println("|                           o                         |");
          System.out.println("=======================================================");
          password = getPass(esql, authorisedUser);
          break;
        }
      }
      System.out.println("|                                                     |");
      System.out.println("|                                                     |");
      System.out.println("|                   Enter new password:               |");
      String newPassword = in.readLine();

      String query = " UPDATE Users SET password = '" + newPassword + "' WHERE login = '" + authorisedUser + "';";
      esql.executeUpdate(query);
      System.out.println("|                           o                         |");
      System.out.println("|                           o                         |");
      System.out.println("|                           o                         |");
      System.out.println("|                    Password updated!                |");
      System.out.println("|                           o                         |");
      System.out.println("|                           o                         |");
      System.out.println("|                           o                         |");
      System.out.println("| Press any key to return to the homepage...");
      in.readLine(); // Wait for the user to press Enter or any key
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }

  }
  public static void updatePhoneNum(GameRental esql, String authorisedUser) {
    try {
      System.out.println("=======================================================");
      System.out.println("|                                                     |");
      System.out.println("|                 Enter new phone number:             |");
      String newPhoneNum = in.readLine();
      System.out.println("   |");

      String query = "  UPDATE Users SET phoneNum = '" + newPhoneNum + "' WHERE login = '" + authorisedUser + "';";
      esql.executeUpdate(query);
      System.out.println("|                           o                         |");
      System.out.println("|                           o                         |");
      System.out.println("|                           o                         |");
      System.out.println("|                                                     |");
      System.out.println("|            Phone number update successful!          |");
      System.out.println("|                                                     |");
      System.out.println("=======================================================");
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public static void updateFavGame(GameRental esql, String authorisedUser) {
    try {
      boolean keepUpdating = true;

      while (keepUpdating) {
        System.out.println("|                           o                         |");
        System.out.println("|                           o                         |");
        System.out.println("|                           o                         |");
        System.out.println("=======================================================");
        System.out.println("|                                                     |");
        System.out.println("|                Current favorite games:              |");

        String selectQuery = "SELECT favGames FROM Users WHERE login = '" + authorisedUser + "'";
        Statement stmt = esql._connection.createStatement();
        ResultSet rs = stmt.executeQuery(selectQuery);
        rs.next();
        String currentFavGames = rs.getString(1);

        System.out.println("|   " + currentFavGames);
        System.out.println("|                                   |");
        System.out.println("|      Enter a favorite game:       |");
        System.out.println("|  (or type 'delete' to remove a game) |");
        System.out.println("|  (or type 'home' to return to home) |");
        System.out.println("|                                   |");
        String newFavGame = in.readLine();
        System.out.println("   |");

        if (newFavGame.equalsIgnoreCase("home")) {
          keepUpdating = false;
          System.out.println("|                    Returning to Home                |");
        } else if (newFavGame.equalsIgnoreCase("delete")) {
          System.out.println("|      Enter the game to remove:     |");
          String gameToRemove = in.readLine();
          currentFavGames = currentFavGames.replaceAll(gameToRemove + ",?", "");
          String updateQuery = "UPDATE Users SET favGames = '" + currentFavGames + "' WHERE login = '" + authorisedUser + "'";
          esql.executeUpdate(updateQuery);
          System.out.println("|          Game removed from favorites                |");
        } else {
          String updatedFavGames = currentFavGames + ", " + newFavGame;
          String updateQuery = "UPDATE Users SET favGames = '" + updatedFavGames + "' WHERE login = '" + authorisedUser + "'";
          esql.executeUpdate(updateQuery);
          System.out.println("|              Game added to favorites                |");
        }

        System.out.println("|                           o                         |");
        System.out.println("|                           o                         |");
        System.out.println("|                           o                         |");
        System.out.println("|              Updated favorite games list            |");
        System.out.println("|                           o                         |");
        System.out.println("|                           o                         |");
        System.out.println("|                           o                         |");
        System.out.println("=======================================================");
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public static void viewCatalog(GameRental esql, String authorisedUser) {
    try {
      System.out.println("|                              o                         |");
      System.out.println("|                           o                         |");
      System.out.println("|                           o                         |");
      System.out.println("==========================================================");
      System.out.println("|                  Viewing Catalog Options               |");
      System.out.println("==========================================================");
      System.out.println("|                                                        |");
      System.out.println("| 1.                    Search By Genre                  |");
      System.out.println("|                                                        |");
      System.out.println("| 2.                   Search By Pricing                 |");
      System.out.println("|                                                        |");
      System.out.println("|--------------------------------------------------------|");

      switch (readChoice()) {
      case 1:
        searchByGenre(esql, authorisedUser);
        break;
      case 2:
        searchByPrice(esql, authorisedUser);
        break;
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public static void searchByGenre(GameRental esql, String authorisedUser) {
    try {
      boolean keepSearching = true;
      while (keepSearching) {
        System.out.println("|                           o                         |");
        System.out.println("|                           o                         |");
        System.out.println("|                           o                         |");
        System.out.println("=======================================================");
        System.out.println("|                     Available Genres:               |");
        String genreQuery = "SELECT DISTINCT genre FROM Catalog;";

        List < List < String >> result1 = esql.executeQueryAndReturnResult(genreQuery);
        for (List < String > row: result1) {
          System.out.println("| " + row.get(0));
        }

        System.out.println("=======================================================");
        System.out.println("|                       Enter a Genre:                |");

        String genreType = in.readLine();
        System.out.println("|");

        System.out.println("|  Games in the specified genre:    |");

        // Fetch all games in the specified genre
        String gamesInGenreQuery = "SELECT gameName FROM Catalog WHERE genre = '" + genreType + "';";
        List < List < String >> result2 = esql.executeQueryAndReturnResult(gamesInGenreQuery);

        System.out.println("=======================================================");
        if (result2.isEmpty()) {
          System.out.println("|   No games found in the specified |");
          System.out.println("|   genre.                          |");
        } else {
          System.out.println("=======================================================");
          System.out.println("|   Games in the specified genre:   |");
          for (List < String > row: result2) {
            System.out.println("|   - " + row.get(0));
          }
        }
        System.out.println("=======================================================");

        // Ask user if they want to search for another genre or quit
        System.out.println("|                                                     |");
        System.out.println("|   Do you want to see another genre? (Y/N): ");
        String userResponse = in.readLine().trim().toUpperCase();
        if (!userResponse.equals("Y")) {
          keepSearching = false;
          System.out.println("|                     Exiting search                  |");
          System.out.println("=======================================================");
        }
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public static void searchByPrice(GameRental esql, String authorisedUser) {
    try {
      boolean keepSearching = true;
      while (keepSearching) {
        System.out.println("=======================================================");
        System.out.println("|                Enter minimum price:                 |");
        String minPriceInput = in.readLine();
        double minPrice = Double.parseDouble(minPriceInput);
        System.out.println("|                Enter maximum price:                 |");
        String maxPriceInput = in.readLine();
        double maxPrice = Double.parseDouble(maxPriceInput);

        System.out.println("=======================================================");
        System.out.println("|                    Choose order:                    |");
        System.out.println("|                   1. Low to High                    |");
        System.out.println("|                   2. High to Low                    |");
        int orderChoice = Integer.parseInt(in.readLine());

        String order = (orderChoice == 1) ? "ASC" : "DESC";
        String priceQuery = "SELECT gameName, price FROM Catalog WHERE price BETWEEN " + minPrice + " AND " + maxPrice + " ORDER BY price " + order + ";";
        List < List < String >> result = esql.executeQueryAndReturnResult(priceQuery);

        System.out.println("=======================================================");
        if (result.isEmpty()) {
          System.out.println("|           No games found within the             |");
          System.out.println("|             specified price range.              |");
        } else {
          System.out.println("|           Games within the specified            |");
          System.out.println("|                 price range:                    |");
          for (List < String > row: result) {
            System.out.println("|   - " + row.get(0) + ": $" + row.get(1));
          }
        }
        System.out.println("=======================================================");

        // Ask user if they want to search for another price range or quit
        System.out.println("|                                   |");
        System.out.println("|   Do you want to search another price range? (Y/N): ");
        String userResponse = in.readLine().trim().toUpperCase();
        if (!userResponse.equals("Y")) {
          keepSearching = false;
          System.out.println("|                    Exiting search                   |");
          System.out.println("=======================================================");
        }
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public static void placeOrder(GameRental esql, String authorisedUser) {
    try {
      // Incrementing the last order number by 1 to use as new order number
      String getLastQuery = "SELECT rentalOrderID FROM RentalOrder ORDER BY rentalOrderID DESC LIMIT 1;";
      List < List < String >> getLast = esql.executeQueryAndReturnResult(getLastQuery);
      String lastID = getLast.get(0).get(0);
      Integer lastOrderId = Integer.valueOf(lastID.substring(lastID.length() - 4));
      Integer lastOrderId_1 = lastOrderId + 1;
      String newOrderId = "gamerentalorder" + String.valueOf(lastOrderId_1);


      String getLastTracking = "SELECT trackingID FROM TrackingInfo ORDER BY trackingID DESC LIMIT 1;";
      List < List < String >> getEnd = esql.executeQueryAndReturnResult(getLastTracking);
      String endID = getEnd.get(0).get(0);
      Integer lastTrackingId = Integer.valueOf(endID.substring(endID.length() - 4));
      Integer lastTrackingId_1 = lastTrackingId + 2;
      String newTrackingId = "trackingid" + String.valueOf(lastTrackingId_1);

      System.out.println("How many games do you want to order?");
      Integer numOfGames = Integer.valueOf(in.readLine());

      // Collect game information
      List < String > gameIds = new ArrayList < > ();
      List < Integer > numCopiesList = new ArrayList < > ();
      Float totalCost = 0.0f;

      for (int i = 0; i < numOfGames; i++) {
        System.out.println("Insert the game ID of the game you want to order");
        String gameId = in.readLine();
        gameIds.add(gameId);

        System.out.println("Insert the amount of copies you want of the game you want to order");
        Integer numCopies = Integer.valueOf(in.readLine());
        numCopiesList.add(numCopies);

        String getCost = "SELECT price FROM Catalog WHERE gameID = '" + gameId + "';";
        List < List < String >> returnCost = esql.executeQueryAndReturnResult(getCost);
        String cost = returnCost.get(0).get(0);
        Float costNum = Float.parseFloat(cost);
        totalCost += costNum * numCopies;
      }

      // Insert the RentalOrder first
      Timestamp currTime = new Timestamp(new Date().getTime());
      Calendar today = Calendar.getInstance();
      today.setTime(currTime);
      today.add(Calendar.DAY_OF_MONTH, 20);
      Timestamp dateDue = new Timestamp(today.getTimeInMillis());

      String putIntoRentalOrder = "INSERT INTO RentalOrder(rentalOrderID, login, noOfGames, totalPrice, orderTimestamp, dueDate) VALUES ('" + newOrderId + "', '" + authorisedUser + "', '" + numOfGames + "', '" + totalCost + "', '" + currTime + "', '" + dateDue + "');";
      esql.executeUpdate(putIntoRentalOrder);

      // Then insert into TrackingOrder
      String putIntoTrackingOrder = "INSERT INTO TrackingInfo(trackingID, rentalOrderID, status, currentLocation, courierName, lastUpdateDate, additionalComments) VALUES ('" + newTrackingId + "', '" + newOrderId + "', 'Out for Delivery', 'DHL', 'Bob', '" + currTime + "', '');";
      esql.executeUpdate(putIntoTrackingOrder);
      List<List<String>> help = esql.executeQueryAndReturnResult(putIntoTrackingOrder);
      System.out.println(help.get(0).get(0));

      // Now insert the games into GamesInOrder
      for (int i = 0; i < numOfGames; i++) {
        String gameId = gameIds.get(i);
        Integer numCopies = numCopiesList.get(i);

        String putIntoGameinOrder = "INSERT INTO GamesInOrder(rentalOrderID, gameID, unitsOrdered) VALUES ('" + newOrderId + "', '" + gameId + "', '" + numCopies + "');";
        esql.executeUpdate(putIntoGameinOrder);
      }

      System.out.printf("Total cost = %.2f\n", totalCost);

    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public static void viewRecentOrders(GameRental esql, String authorisedUser) {
    try {
      System.out.println("=======================================================");
      System.out.println("|            Viewing 5 Most Recent Orders             |");
      System.out.println("=======================================================");
      System.out.println("|                                                     |");

      String query = "SELECT rentalOrderID, noOfGames, totalPrice, orderTimestamp, dueDate FROM RentalOrder " +
        "WHERE login = '" + authorisedUser + "' ORDER BY orderTimestamp DESC LIMIT 5;";
      List < List < String >> result = esql.executeQueryAndReturnResult(query);

      if (result.isEmpty()) {
        System.out.println("|          No recent orders found.                    |");
      } else {
        for (List < String > row: result) {
          System.out.println("| Order ID: " + row.get(0));
          System.out.println("| Number of Games: " + row.get(1));
          System.out.println("| Total Price: " + row.get(2));
          System.out.println("| Order Timestamp: " + row.get(3));
          System.out.println("| Due Date: " + row.get(4));
          System.out.println("|-----------------------------------------------------|");
        }
      }

      System.out.println("|                                                     |");
      System.out.println("=======================================================");
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public static void viewAllOrders(GameRental esql, String authorisedUser) {
    try {
      System.out.println("=======================================================");
      System.out.println("|             Viewing All Previous Orders             |");
      System.out.println("=======================================================");
      System.out.println("|                                                     |");

      String query = "SELECT rentalOrderID, noOfGames, totalPrice, orderTimestamp, dueDate FROM RentalOrder WHERE login = '" + authorisedUser + "';";
      List < List < String >> result = esql.executeQueryAndReturnResult(query);

      if (result.isEmpty()) {
        System.out.println("|            No previous orders found                 |");
      } else {
        System.out.println("=======================================================");
        for (List < String > row: result) {
          System.out.println("| Order ID         : " + row.get(0));
          System.out.println("| No. of Games     : " + row.get(1));
          System.out.println("| Total Price      : " + row.get(2));
          System.out.println("| Order Timestamp  : " + row.get(3));
          System.out.println("| Due Date         : " + row.get(4));
          System.out.println("-------------------------------------------------------");
        }
      }

      System.out.println("|                                                     |");
      System.out.println("=======================================================");
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public static void viewOrderInfo(GameRental esql, String authorisedUser) {
    try {
      System.out.println("=======================================================");
      System.out.println("|          Viewing A Specific Recent Order            |");
      System.out.println("=======================================================");
      System.out.println("|                                                     |");
      System.out.println("|        Insert the OrderId you want to view          |");
      System.out.println("|                                                     |");
      System.out.println("=======================================================");

      String orderId = in.readLine();
      String queryGames = "SELECT C.gameName FROM RentalOrder R, TrackingInfo T, GamesInOrder G, Catalog C WHERE R.login = '" + authorisedUser + "' AND R.rentalOrderID = '" + orderId + "' AND R.rentalOrderID = T.rentalOrderID AND R.rentalOrderID = G.rentalOrderID AND G.gameID = C.gameID;";
      String queryInfo = "SELECT T.trackingID FROM TrackingInfo T WHERE T.rentalOrderID = '" + orderId + "';";
      // String queryInfo = "SELECT R.orderTimestamp, R.dueDate, R.totalPrice, T.trackingID FROM RentalOrder R, TrackingInfo T WHERE R.login = '" + authorisedUser + "' AND R.rentalOrderID = '" + orderId + "' AND T.rentalOrderID = '" + orderId + "';";
      List < List < String >> info = esql.executeQueryAndReturnResult(queryInfo);
      System.out.println(info);
      List < String > order = info.get(0);
      List < List < String >> game = esql.executeQueryAndReturnResult(queryGames);
      System.out.println("=======================================================");
      System.out.println("|                      Order Info                     |");
      System.out.println("=======================================================");
      System.out.println("| Order Timestamp: " + order.get(0));
      System.out.println("| Due Date: " + order.get(1));
      System.out.println("| Total Price: " + order.get(2));
      System.out.println("| TrackingID: " + order.get(3));
      System.out.println("| Ordered Games: " + game);
      System.out.println("=======================================================");
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public static void viewTrackingInfo(GameRental esql, String authorisedUser) {
    try {
      boolean keepGoing = true;

      while (keepGoing) {
        System.out.println("=======================================================");
        System.out.println("|                                                     |");
        System.out.println("|                  Enter a Tracking ID:               |");
        System.out.println("|                                                     |");
        System.out.println("=======================================================");
        String trackingID = in.readLine();

        String query = "SELECT T.courierName, T.rentalOrderID, T.currentLocation, T.status, T.lastUpdateDate, T.additionalComments " +
          "FROM TrackingInfo T, RentalOrder R WHERE trackingID = '" + trackingID + "'" + 
          " AND T.rentalOrderID = R.rentalOrderID AND R.login = '" + authorisedUser + "';";
        int resultCheck = esql.executeQuery(query);

        if (resultCheck > 0) {
          List < List < String >> result = esql.executeQueryAndReturnResult(query);
          List < String > trackingInfo = result.get(0);
          System.out.println("=======================================================");
          System.out.println("|                 Tracking Information                |");
          System.out.println("=======================================================");
          System.out.println("| Courier Name: " + trackingInfo.get(0));
          System.out.println("| Rental Order ID: " + trackingInfo.get(1));
          System.out.println("| Current Location: " + trackingInfo.get(2));
          System.out.println("| Status: " + trackingInfo.get(3));
          System.out.println("| Last Updated Date: " + trackingInfo.get(4));
          System.out.println("| Additional Comments: " + trackingInfo.get(5));
          System.out.println("=======================================================");
        } else {
          System.out.println("No tracking information for/no permission to view trackingID: " + trackingID);
        }

        // Prompt user to enter another tracking ID or quit
        System.out.println("Do you want to enter another tracking ID? (Y/N)");
        String response = in.readLine();
        if (!response.equalsIgnoreCase("Y")) {
          keepGoing = false;
          System.out.println("Returning to homepage...");
        }
      }
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  public static void updateTrackingInfo(GameRental esql, String authorisedUser) {
    try {
      String query = "SELECT role FROM Users WHERE login = '" + authorisedUser + "';";
      List < List < String >> roleQuery = esql.executeQueryAndReturnResult(query);
      String userRole = roleQuery.get(0).get(0);
      System.out.println(userRole);
      if (userRole.trim().equals("manager") || userRole.trim().equals("manager")) {
        System.out.println("Enter tracking ID you would like to update");
        String trackingIdUpdate = in.readLine();
        System.out.println("|                           o                         |");
        System.out.println("|                           o                         |");
        System.out.println("|                           o                         |");
        System.out.println("=======================================================");
        System.out.println("|                  Update Tracking Info               |");
        System.out.println("=======================================================");
        System.out.println("|                                                     |");
        System.out.println("| 1.                  Update status                   |");
        System.out.println("|                                                     |");
        System.out.println("| 2.              Update current location             |");
        System.out.println("|                                                     |");
        System.out.println("| 3.               Update courrier name               |");
        System.out.println("|                                                     |");
        System.out.println("| 4.              Add additional comments             |");
        System.out.println("|                                                     |");
        System.out.println("=======================================================");
        // System.out.println("|      Please make your choice:     |");
        switch (readChoice()) {
        case 1:
          updateTrackingStatus(esql, trackingIdUpdate);
          break;
        case 2:
          updateCurrentLocation(esql, trackingIdUpdate);
          break;
        case 3:
          updateCourierName(esql, trackingIdUpdate);
          break;
          // case 4: addAdditionalComments(); break;
        }
      } else {
        System.out.println("You do not have permissions to edit");
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public static void updateTrackingStatus(GameRental esql, String trackingIdUpdate) {
    try {
      System.out.println("=======================================================");
      System.out.println("|                    Update Status                    |");
      System.out.println("=======================================================");
      System.out.println("|                                                     |");
      System.out.println("|                  Options . . .                      |");
      System.out.println("|                                                     |");
      System.out.println("|  1. Change to:  Out for Delivery                    |");
      System.out.println("|  2. Change to:  Delivered                           |");
      System.out.println("|  3. Change to:  In Transit                          |");
      System.out.println("|  4. Change to:  Delayed                             |");
      System.out.println("|  5. Change to:  Ready for Pickup                    |");
      System.out.println("|  6. Change to:  Attempted Delivery                  |");
      System.out.println("|  7. Change to:  Arrived at Facility                 |");
      System.out.println("|  8. Change to:  Returned to Sender                  |");
      System.out.println("|                                                     |");
      System.out.println("=======================================================");

      switch (readChoice()) {
      case 1:
        String case1 = "Out for Delivery";
        String update1 = "UPDATE TrackingInfo SET status = '" + case1 + "' WHERE trackingID = '" + trackingIdUpdate + "';";
        esql.executeUpdate(update1);
        break;
      case 2:
        String case2 = "Delivered";
        String update2 = "UPDATE TrackingInfo SET status = '" + case2 + "' WHERE trackingID = '" + trackingIdUpdate + "';";
        esql.executeUpdate(update2);
        break;
      case 3:
        String case3 = "In Transit";
        String update3 = "UPDATE TrackingInfo SET status = '" + case3 + "' WHERE trackingID = '" + trackingIdUpdate + "';";
        esql.executeUpdate(update3);
        break;
      case 4:
        String case4 = "Delayed";
        String update4 = "UPDATE TrackingInfo SET status = '" + case4 + "' WHERE trackingID = '" + trackingIdUpdate + "';";
        esql.executeUpdate(update4);
        break;
      case 5:
        String case5 = "Ready for Pickup";
        String update5 = "UPDATE TrackingInfo SET status = '" + case5 + "' WHERE trackingID = '" + trackingIdUpdate + "';";
        esql.executeUpdate(update5);
        break;
      case 6:
        String case6 = "Attempted Delivery";
        String update6 = "UPDATE TrackingInfo SET status = '" + case6 + "' WHERE trackingID = '" + trackingIdUpdate + "';";
        esql.executeUpdate(update6);
        break;
      case 7:
        String case7 = "Arrived at Facility";
        String update7 = "UPDATE TrackingInfo SET status = '" + case7 + "' WHERE trackingID = '" + trackingIdUpdate + "';";
        esql.executeUpdate(update7);
        break;
      case 8:
        String case8 = "Returned to Sender";
        String update8 = "UPDATE TrackingInfo SET status = '" + case8 + "' WHERE trackingID = '" + trackingIdUpdate + "';";
        esql.executeUpdate(update8);
        break;
      default:
        System.out.println("|               Invalid option!                      |");
        break;
      }
      System.out.println("=======================================================");
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public static void updateCurrentLocation(GameRental esql, String trackingIdUpdate) {
    try {
      System.out.println("Insert new location:");
      System.out.println("(Format: city,State. Ex: Austin,TX)");
      String newLocation = in.readLine();
      String updateLocation = "UPDATE TrackingInfo SET currentLocation = '" + newLocation + "' WHERE trackingID = '" + trackingIdUpdate + "';";
      esql.executeUpdate(updateLocation);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public static void updateCourierName(GameRental esql, String trackingIdUpdate) {
    try {
      System.out.println("Insert new courier name:");
      String newName = in.readLine();
      String updateName = "UPDATE TrackingInfo SET courierName = '" + newName + "' WHERE trackingID = '" + trackingIdUpdate + "';";
      esql.executeUpdate(updateName);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public static void updateCatalog(GameRental esql, String authorisedUser) {}
  public static void updateUser(GameRental esql, String authorisedUser) {
    try {
      String query = "SELECT role FROM Users WHERE login = '" + authorisedUser + "';";
      List < List < String >> roleQuery = esql.executeQueryAndReturnResult(query);
      String userRole = roleQuery.get(0).get(0);
      System.out.println(userRole);
      if (userRole.trim().equals("manager")) {
        System.out.println("|                           o                         |");
        System.out.println("|                           o                         |");
        System.out.println("|                           o                         |");
        System.out.println("=======================================================");
        System.out.println("|             Update a Customer's Profile             |");
        System.out.println("=======================================================");
        System.out.println("|                                                     |");
        System.out.println("| 1.                  Update login                    |");
        System.out.println("|                                                     |");
        System.out.println("| 2.                  Update role                     |");
        System.out.println("|                                                     |");
        System.out.println("| 3.       Update number of overdue games             |");
        System.out.println("|                                                     |");
        System.out.println("|-----------------------------------------------------|");
        // System.out.println("|      Please make your choice:     |");
        switch (readChoice()) {
        case 1:
          workerUpdateLogin(esql, authorisedUser);
          break;
        case 2:
          workerUpdateRole(esql, authorisedUser);
          break;
        case 3:
          updateOverdueGames(esql, authorisedUser);
          break;
        }
      } else {
        System.out.println("You do not have permissions to edit");
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public static void workerUpdateLogin(GameRental esql, String authorisedUser) {
    try {
      System.out.println("=======================================================");
      System.out.println("|               Update User Login                     |");
      System.out.println("=======================================================");
      System.out.println("|                                                     |");
      System.out.println("|   Enter the user you want to update login:          |");
      System.out.println("|                                                     |");
      String userUpdate = in.readLine();
      System.out.println("|                                                     |");
      System.out.println("|            Enter the new login name:                |");
      System.out.println("|                                                     |");
      String newUserLogin = in.readLine();

      String newLoginQuery = "UPDATE Users SET login = '" + newUserLogin + "' WHERE login = '" + userUpdate + "';";
      esql.executeUpdate(newLoginQuery);

      System.out.println("|                                                     |");
      System.out.println("|                Login Update Successful!             |");
      System.out.println("|                                                     |");
      System.out.println("=======================================================");
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public static void workerUpdateRole(GameRental esql, String authorisedUser) {
    try {
      System.out.println("=======================================================");
      System.out.println("|               Update User Role                      |");
      System.out.println("=======================================================");
      System.out.println("|                                                     |");
      System.out.println("| Enter user you want to update role:                 |");
      System.out.println("|                                                     |");
      String userUpdate = in.readLine();
      System.out.println("|                                                     |");
      System.out.println("| Set user role to 'customer', 'employee', or 'manager':");
      System.out.println("|                                                     |");
      String newRole = in.readLine().trim();


      if (newRole.equals("customer") || newRole.equals("employee") || newRole.equals("manager")) {
        String newLoginQuery = "UPDATE Users SET role = '" + newRole + "' WHERE login = '" + userUpdate + "';";
        esql.executeUpdate(newLoginQuery);
        System.out.println("|                                                     |");
        System.out.println("|                Role Update Successful!              |");
        System.out.println("|                                                     |");
      } else {
        System.out.println("|                                                     |");
        System.out.println("|                Invalid input.                       |");
        System.out.println("|     Press any key to return to the main menu.       |");
        in.readLine();
      }
      System.out.println("=======================================================");
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public static void updateOverdueGames(GameRental esql, String authorisedUser) {
    try {
      System.out.println("=======================================================");
      System.out.println("|              Update Overdue Games                   |");
      System.out.println("=======================================================");
      System.out.println("|                                                     |");
      System.out.println("| Enter user you want to update number of overdue games:");
      System.out.println("|                                                     |");
      String userUpdate = in.readLine();
      System.out.println("|                                                     |");
      System.out.println("| Enter number of overdue games:                      |");
      System.out.println("|                                                     |");
      Integer newNumber = Integer.valueOf(in.readLine());
      System.out.println("|                                                     |");

      String newLoginQuery = "UPDATE Users SET numOverdueGames = " + newNumber + " WHERE login = '" + userUpdate + "';";
      esql.executeUpdate(newLoginQuery);

      System.out.println("=======================================================");
      System.out.println("|                Update Successful!                   |");
      System.out.println("=======================================================");
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

} //end GameRental