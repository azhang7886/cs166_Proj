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
      System.out.println("==================================");
      System.out.println("|                o               |");
      System.out.println("|                o               |");
      System.out.println("|                o               |");
      System.out.println("|     Connecting to database     |");
      System.out.println("|                o               |");
      System.out.println("|                o               |");
      System.out.println("|                o               |");
      
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         //System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         // System.out.println("|                o               |");
         System.out.println("|Database Connection Successful! |");
         System.out.println("|       Loading Main Menu        |");
         System.out.println("|                o               |");
         System.out.println("|                o               |");
         System.out.println("|                o               |");
         System.out.println("==================================");
         System.out.println("|           Main Menu            |");
         System.out.println("==================================");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end GameRental

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
		 if(outputHeader){
			for(int i = 1; i <= numCol; i++){
			System.out.print(rsmd.getColumnName(i) + "\t");
			}
			System.out.println();
			outputHeader = false;
		 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
        List<String> record = new ArrayList<String>();
		for (int i=1; i<=numCol; ++i)
			record.add(rs.getString (i));
        result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       while (rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
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
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            GameRental.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      GameRental esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the GameRental object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new GameRental (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            
            System.out.println("====================================");
            System.out.println("|    Type 1 to create an account.   |");
            System.out.println("|                                   |");
            System.out.println("|    Type 2 to login to account.    |");
            System.out.println("|                                   |");
            System.out.println("|    Type 9 to exit the app.        |");
            System.out.println("====================================");
            // System.out.println("1. Create an account!");
            // System.out.println("2. Log in");
            // System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("|   Invalid selection! try again     |"); break;
            }//end switch
            if (authorisedUser != null) {
              System.out.println("|                  o                |");
              System.out.println("|                  o                |");
              System.out.println("|                  o                |");
              System.out.println("|           Successful Login!       |");
              System.out.println("|           Loading Homepage        |");
              System.out.println("|                  o                |");
              System.out.println("|                  o                |");
              System.out.println("|                  o                |");
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("|-----------------------------------|");
                System.out.println("|              MY HOME              |");
                System.out.println("|-----------------------------------|");
                System.out.println("|                                   |");
                System.out.println("|                                   |");
                System.out.println("| 1.         View Profile           |");
                System.out.println("|                                   |");
                System.out.println("| 2.       Update Profile           |");
                System.out.println("|                                   |");
                System.out.println("| 3.        View Catalog            |");
                System.out.println("|                                   |");
                System.out.println("| 4.     Place Rental Order         |");
                System.out.println("|                                   |");
                System.out.println("| 5. View Full Rental Order History |");
                System.out.println("|                                   |");
                System.out.println("| 6. View Past 5 Rental Orders      |");
                System.out.println("|                                   |");
                System.out.println("| 7. View Rental Order Information  |");
                System.out.println("|                                   |");
                System.out.println("| 8. View Tracking Information      |");
                System.out.println("|                                   |");
                System.out.println("|                                   |");

                //the following functionalities basically used by employees & managers
                System.out.println("|-----------------------------------|");
                System.out.println("|  Employee and Managers Access*    |");
                System.out.println("|-----------------------------------|");
                System.out.println("|                                   |");
                System.out.println("| 9. Update Tracking Information    |");
                System.out.println("|                                   |");
                System.out.println("|-----------------------------------|");

                //the following functionalities basically used by managers
                System.out.println("|-----------------------------------|");
                System.out.println("|         Managers Access*          |");
                System.out.println("|-----------------------------------|");
                System.out.println("|                                   |");
                System.out.println("|       10. Update Catalog          |");
                System.out.println("|        11. Update User            |");
                System.out.println("|                                   |");
                System.out.println("|-----------------------------------|");
                System.out.println("|-----------------------------------|");
                System.out.println("|                                   |");
                System.out.println("|            20. Log out            |");
                System.out.println("|                                   |");
                System.out.println("|-----------------------------------|");
                System.out.println("|-----------------------------------|");
                switch (readChoice()){
                   case 1: viewProfile(esql, authorisedUser); break;
                   case 2: updateProfile(esql, authorisedUser); break;
                   case 3: viewCatalog(esql); break;
                   case 4: placeOrder(esql); break;
                   case 5: viewAllOrders(esql); break;
                   case 6: viewRecentOrders(esql); break;
                   case 7: viewOrderInfo(esql); break;
                   case 8: viewTrackingInfo(esql); break;
                   case 9: updateTrackingInfo(esql); break;
                   case 10: updateCatalog(esql); break;
                   case 11: updateUser(esql); break;



                   case 20: usermenu = false; break;
                   default : System.out.println("|    Invalid selection! try again   |"); break;
                           
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
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
   }//end Greeting  

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.println("|                                   |");
         System.out.println("|     Please make your choice:      |");
         System.out.println("|                                   |");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("|    Invalid selection! try again   |");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user
    */
   public static void CreateUser(GameRental esql){
        try {
            System.out.println("====================================");
            System.out.println("|           Create User             |");
            System.out.println("====================================");    
            System.out.println("|                                   |");
            System.out.println("|                                   |");
            
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
            System.out.println("|                  o                |");
            System.out.println("|                  o                |");
            System.out.println("|                  o                |");
            System.out.println("|          Creating account         |");
            System.out.println("|                  o                |");
            System.out.println("|                  o                |");
            System.out.println("|                  o                |");
            System.out.println("|     User created successfully!    |");
            System.out.println("|                                   |");
            System.out.println("====================================="); 
        } catch(Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }


   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(GameRental esql){
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
         }
         else {
            return null;
         }
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
      return null;
   }//end

// Rest of the functions definition go in here

   public static void viewProfile(GameRental esql, String authorisedUser) {
      try{
         String login = authorisedUser;
         String query = "SELECT * FROM Users WHERE login = '" + login + "';";

         esql.executeQueryAndPrintResult(query);
      }catch(Exception e){
        System.err.println (e.getMessage ());
      }
   }
   public static void updateProfile(GameRental esql, String authorisedUser) {
      try {
         System.out.println("|                  o                |");
         System.out.println("|                  o                |");
         System.out.println("|                  o                |");
         System.out.println("=====================================");
         System.out.println("|         Update Your Profile       |");
         System.out.println("=====================================");
         System.out.println("|                                   |");
         System.out.println("| 1.       Update password          |");
         System.out.println("|                                   |");
         System.out.println("| 2.      Update phone number       |");
         System.out.println("|                                   |");
         System.out.println("| 3.     Update favorite games      |");
         System.out.println("|                                   |");
         System.out.println("|-----------------------------------|");
         // System.out.println("|      Please make your choice:     |");
         switch(readChoice()) {
            case 1: updatePassword(esql, authorisedUser); break;
            case 2: updatePhoneNum(esql, authorisedUser); break;
            case 3: updateFavGame(esql, authorisedUser); break;
         }
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }

   public static String getPass(GameRental esql, String authorisedUser) {
      try {
         String query = "SELECT password FROM Users WHERE login = '" + authorisedUser + "';";
         int correct = esql.executeQueryAndPrintResult(query);
         List<List<String>> currPass = esql.executeQueryAndReturnResult(query);
         String correctPass = currPass.get(0).get(0);
         return correctPass;
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
      return null;
   }

   public static void updatePassword(GameRental esql, String authorisedUser) {
      try {
         System.out.println("=====================================");
         System.out.println("|                                   |");
         System.out.println("|     Enter current password:");
         String password = in.readLine().trim();
         System.out.println("|");
         String correctPassword = getPass(esql, authorisedUser);
         correctPassword.trim();
         while (!password.equals(correctPassword)) {
            System.out.println("=====================================");
            System.out.println("|                                   |");
            System.out.println("|           Wrong password          |");
            System.out.println("|        1. reenter password        |");
            System.out.println("|            2. Go back             |");
            switch(readChoice()) {
               case 1: 
                  System.out.println("|                                   |");
                  System.out.println("           reenter password: ");
                  password = in.readLine();
                  System.out.println("|                                   |");
                  break;
               case 2:
                  System.out.println("|                  o                |");
                  System.out.println("|                  o                |");
                  System.out.println("|                  o                |");
                  System.out.println("|             Going back            |");
                  System.out.println("|                  o                |");
                  System.out.println("|                  o                |");
                  System.out.println("|                  o                |");
                  System.out.println("=====================================");
                  password = getPass(esql, authorisedUser);
                  break;
            }
         }
         System.out.println("|                                   |");
         System.out.println("|                                   |");
         System.out.println("|         Enter new password:       |");
         String newPassword = in.readLine();

         String query = " UPDATE Users SET password = '" + newPassword + "' WHERE login = '" + authorisedUser + "';";
         esql.executeUpdate(query);
         System.out.println("|                                   |");
         System.out.println("|          Password updated!        |");
         System.out.println("|                                   |");
         System.out.println("|                  o                |");
         System.out.println("|                  o                |");
         System.out.println("|                  o                |");
         System.out.println("|          Returning to Home        |");
         System.out.println("|                  o                |");
         System.out.println("|                  o                |");
         System.out.println("|                  o                |");
         System.out.println("=====================================");
         System.out.println("=====================================");
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
      
   }
   public static void updatePhoneNum(GameRental esql, String authorisedUser) {
      try {
         System.out.println("=====================================");
         System.out.println("|                                   |");
         System.out.println("|      Enter new phone number:      |");
         String newPhoneNum = in.readLine();
         System.out.println("   |");

         String query = "  UPDATE Users SET phoneNum = '" + newPhoneNum + "' WHERE login = '" + authorisedUser + "';";
         esql.executeUpdate(query);

         System.out.println("|                                   |");
         System.out.println("|  Phone number update successful!  |");
         System.out.println("|                                   |");
         System.out.println("=====================================");
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }

   public static void updateFavGame(GameRental esql, String authorisedUser) {
      
   }

   public static void updateProfile(GameRental esql) {


   }
   public static void viewCatalog(GameRental esql) {}
   public static void placeOrder(GameRental esql) {}
   public static void viewAllOrders(GameRental esql) {}
   public static void viewRecentOrders(GameRental esql) {}
   public static void viewOrderInfo(GameRental esql) {}
   public static void viewTrackingInfo(GameRental esql) {}
   public static void updateTrackingInfo(GameRental esql) {}
   public static void updateCatalog(GameRental esql) {}
   public static void updateUser(GameRental esql) {}


}//end GameRental

