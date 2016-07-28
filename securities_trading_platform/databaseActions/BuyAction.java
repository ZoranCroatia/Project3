package securities_trading_platform.databaseActions;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * This class connects to the database, and creates table in the database if it is not
 * already created. It then inserts or updates rows in the database when user presses
 * buy button in the JTable tableTrading.
 */
public class BuyAction {

	public	BuyAction(String name_of_the_corporation, String trading_symbol, String stock_exchange, int quantity, 
			double buying_value) {

		Connection con = null;
		Statement statement = null;

		try {
			String ourTable = null;

			// First, we need to add driver to the "Java Build Path" which can be downloaded from
			// https://jdbc.postgresql.org/download.html
			Class.forName("org.postgresql.Driver");

			// 5432 is the port.
			// stockstable is the name of the database that needs to be created before we run this app.
			String url = "jdbc:postgresql://localhost:5432/stockstable";
			String username = "postgres";
			String password = "svetaana14";
			con = DriverManager.getConnection(url, username, password);
			statement = con.createStatement();

			// First we need to check if the table with the name portfolio exists in our database
			DatabaseMetaData md = con.getMetaData();
			ResultSet rs = md.getTables(null, null, "portfolio", null);
			while (rs.next()) {
				ourTable = rs.getString(3);
			}

			if (ourTable == null) {
				// Create new table if it doesn't exist
				statement.executeUpdate("CREATE TABLE portfolio" +		
						" (id BIGINT PRIMARY KEY, " +
						" name_of_the_corporation TEXT, " +
						" trading_symbol TEXT, " +
						" stock_exchange TEXT, " +
						" quantity INT, " +
						" buying_value MONEY)");
			}			

			ResultSet result = null; 
			
			// I know this is not perfect solution but I had problems with EXISTS
			result = statement.executeQuery("SELECT * FROM portfolio WHERE trading_symbol = '"+trading_symbol+"'");

			if (!result.isBeforeFirst()){   	// Data does not exist, insert new row with the data
				// nextval('zorannew') is used to auto-increase id by 1, every time new row is being added
				// It is interested that buying_value needs to be divided by 10 for the German (Postgres database locale), 
				// but it doesn't need to be divided for the US locale
				statement.executeUpdate("INSERT INTO portfolio VALUES (nextval('zorannew'), '"+name_of_the_corporation+"', "
						+ "'"+trading_symbol+"', '"+stock_exchange+"', '"+quantity+"', '"+buying_value/10+"')");
			}

			else {	// Data exists, just increase quantity and buying_value
				statement.executeUpdate("UPDATE portfolio SET quantity = (quantity + '"+quantity+"'),  buying_value = (buying_value + "
						+ "'"+buying_value/10+"') WHERE trading_symbol = '"+trading_symbol+"'");
			}
		}

		catch (SQLException e) {
			System.out.println(e);
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}

		finally {		// objects must be closed, in this order: 1. statement, 2. connection
			try {
				if (statement != null) statement.close();
				if (con != null) con.close();
			}
			catch (SQLException e) {
				System.out.println(e);
			}
		}
	}
}