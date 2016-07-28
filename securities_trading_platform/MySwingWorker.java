package securities_trading_platform;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.SwingWorker;

import securities_trading_platform.gui.table.TablePortfolio;

/*
 * On a background thread SwingWorker, it connects to the Postgres database (local), 
 * gets the data, formats it, and for each row from the database table it adds row
 * into the JTable TablePortfolio. It repeats all this every 2 seconds.
 */
public class MySwingWorker extends SwingWorker<Void,Void> {

	private final TablePortfolio modelPortfolio;

	public MySwingWorker (TablePortfolio modelPortfolio) {
		this.modelPortfolio = modelPortfolio;
	}
	
	// The Void class is an uninstantiable placeholder class to hold a 
	// reference to the Class object representing the Java keyword void.
	@Override
	protected Void doInBackground() throws Exception {
		while(true) { 				// execute it forever

			Thread.sleep(2000);

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

				// Will delete all the rows in the table
				modelPortfolio.setRowCount(0);

				ResultSet rs1 = statement.executeQuery("SELECT * FROM portfolio");
				while (rs1.next()) {	// next() moves current cursor in ResultSet object to next available row
					String corporation = rs1.getString("name_of_the_corporation");
					String ticker = rs1.getString("trading_symbol");
					String exchange = rs1.getString("stock_exchange");
					int quantity = rs1.getInt("quantity");
					// Will format quantity into ###.###.### ...
					String quantityFormatted = NumberFormat.getNumberInstance(Locale.GERMANY).format(quantity);
					String buying_value = rs1.getString("buying_value");

					Object[] dataFromDatabase = {corporation, ticker, exchange, quantityFormatted, buying_value};

					// Will add new row for each row from the table portfolio from the database
					modelPortfolio.addRow(dataFromDatabase);
				}

				// This is tested and it works, but it is not necessary here.
				/*
				ResultSet rs1 = statement.executeQuery("SELECT * FROM portfolio");
				List<Object> list = new ArrayList<Object>();
				while (rs1.next()) {	// next() moves current cursor in ResultSet object to next available row
					list.add(rs1.getString("name_of_the_corporation"));
					list.add(rs1.getString("trading_symbol"));
					list.add(rs1.getString("stock_exchange"));
					list.add(rs1.getString("quantity"));
					list.add(rs1.getString("buying_value"));
				}

				// Convert list into an array
				Object [] array = list.toArray(new Object[list.size()]);

				// Number 5 is used because 5 columns are in the database. So, this will split 
				// 1-dimensional array into 2-dimensional array. 2-dimensional array
				// will have 5 columns and number of rows depends on the number of rows in the database
				Object array2d[][] = new Object[list.size()/5][5];
				for (int i = 0; i < list.size()/5; i++) {
					for (int j = 0; j < 5; j++) {
						array2d[i][j] = array[i*5 + j%5]; 
					 }
				 }

				// To check if it works - it does
				System.out.println(Arrays.deepToString(array2d));
				 */

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
}