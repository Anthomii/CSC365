import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.util.*;

public class InnReservations {

   public static void main(String[] args) {
      try {
         InnReservations db = new InnReservations();
	    hp.demo3();
	} catch (SQLException e) {
	    System.err.println("SQLException: " + e.getMessage());
	}
   }
}
