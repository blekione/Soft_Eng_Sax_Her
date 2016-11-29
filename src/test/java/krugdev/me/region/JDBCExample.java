package krugdev.me.region;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCExample {
	// JDBC driver name and database URL
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://0.0.0.0:6603/sax_her";

	   //  Database credentials
	   static final String USER = "root";
	   static final String PASS = "mypassword";
	   
	   public static void deleteTable() {
		   Connection conn = null;
		   Statement stmt = null;
		   try{
		      //STEP 2: Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");

		      //STEP 3: Open a connection
//		      System.out.println("Connecting to a selected database...");
		      conn = DriverManager.getConnection(DB_URL, USER, PASS);
//		      System.out.println("Connected database successfully...");
		      
		      //STEP 4: Execute a query
//		      System.out.println("Deleting table in given database...");
		      stmt = conn.createStatement();
		      
		      String sql = "DROP TABLE RegionSiteCampaigns";
		 
		      stmt.executeUpdate(sql);
		      
		      sql = "DROP TABLE RegionSite";
		      
		      stmt.executeUpdate(sql);
		      
		      sql = "DROP TABLE MarketingCampaign";
		      stmt.executeUpdate(sql);
//		      System.out.println("Table  deleted in given database...");
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
	   }
}
