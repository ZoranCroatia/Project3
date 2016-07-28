package securities_trading_platform.databaseActions;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JOptionPane;

import securities_trading_platform.gui.table.TableTransactions;

/*
 * This class connects to the database. 
 * It then updates rows in the database when user presses
 * sell button in the JTable tableTrading.
 */
public class SellAction {
	
	public	SellAction(String name_of_the_corporation, String trading_symbol, String stock_exchange, int quantity, 
			double selling_value, TableTransactions modelTransactions) {

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

			if (!result.isBeforeFirst()){   	// Data does not exist
				JOptionPane.showMessageDialog(null, "Quantity of this stock in your portfolio is 0. "
						+ "You can't sell something that you don't have"
						+ " (you can in reality, but not in this simulation).");
			}

			else {	// Data exists, just decrease quantity and buying_value (yes buying_value)

				int quantityInPortfolio = 0;

				while (result.next()) {	// next() moves current cursor in ResultSet object to next available row
					quantityInPortfolio = result.getInt("quantity");
				}

				// If the user wants to sell less quantity then he has in his portfolio...
				if (quantity <= quantityInPortfolio) {
					statement.executeUpdate("UPDATE portfolio SET quantity = (quantity - '"+quantity+"'),  buying_value = (buying_value - "
							+ "'"+selling_value/10+"') WHERE trading_symbol = '"+trading_symbol+"'");
					
					// Will format quantity into ###.###.###
					String quantityFormatted = NumberFormat.getNumberInstance(Locale.GERMANY).format(quantity);
					
					// Will be needed for formatting selling_value
					java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.GERMANY);
					
					// Will format selling_value into ###.###.###,## €
					String selling_valueFormatted = format.format(selling_value);
										
					Object[] sellButtonPressed = {name_of_the_corporation, trading_symbol, stock_exchange, quantityFormatted, selling_valueFormatted};

					// Adds row in table TableTransactions
					modelTransactions.addRow(sellButtonPressed);
				}
				
				// If the user wants to sell more quantity then he has in his portfolio...
				if (quantity > quantityInPortfolio) {
					JOptionPane.showMessageDialog(null, "You can't sell more then you have in your portfolio"
							+ " (you can in reality, but not in this simulation).");
				}
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