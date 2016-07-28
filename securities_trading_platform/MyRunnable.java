package securities_trading_platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import securities_trading_platform.gui.table.TableTrading;

/*
 * This class is used to, on a background thread, to fetch data from the
 * Yahoo finance and to convert it into an appropriate formats. It then sends
 * that data to the table TableTrading.
 */
public class MyRunnable implements Runnable {

	private final String rowNames;
	private final int threadLocation;
	private final TableTrading modelTrading;

	public MyRunnable(String rowNames, int threadLocation, TableTrading modelTrading) {
		this.rowNames = rowNames;
		this.threadLocation = threadLocation;
		this.modelTrading = modelTrading;
	}

	// This method fetches data from the Yahoo finance and converts it into
	// an appropriate formats
	@Override
	public void run() {
		while(true) {		// execute it forever
			try {
				double bidPrice;
				double askPrice;

				String column9;
				String column10;

				// Will need this to convert all numbers in the table (except quantity) into format x.xxx,xx €
				java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.GERMANY);

				Thread.sleep(100);

				// More info can be found at: http://www.jarloo.com/yahoo_finance/
				URL yahoofinance = new URL("http://finance.yahoo.com/d/quotes.csv?s=" + rowNames + "&f=nsxbap2");
				URLConnection yc = yahoofinance.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
				String inputLine = in.readLine();
				String[] data = inputLine.split(",");

				String rawColumn0 = data[0];
				// We need to remove " at the beginning and " at the end of the String
				String column0 = rawColumn0.substring(1, rawColumn0.length()-1);

				String rawColumn1 = data[1];
				// We need to remove " at the beginning and " at the end of the String
				String column1 = rawColumn1.substring(1, rawColumn1.length()-1);

				String rawColumn2 = data[2];
				// We need to remove " at the beginning and " at the end of the String
				String column2 = rawColumn2.substring(1,4);

				switch (column2) {
				case "GER": column2 = "Frankfurt";
				break;
				case "PAR": column2 = "Paris";
				break;		
				case "VIE": column2 = "Vienna";
				break;
				case "MIL": column2 = "Milan";
				break;
				case "AMS": column2 = "Amsterdam";
				break;
				}

				String column3 = data[3];
				String column4 = data[4];

				String column5 = data[5];
				if (column5.equals("N/A")){
					column5 = "Unknown";
				}
				else {
					// We don't need: " and %". Plus we only need 2 decimals
					String version1rawColumn5 = column5.substring(1,6);
					// Now we need to add %		
					String version2rawColumn5 =  version1rawColumn5 + "%";
					column5 = version2rawColumn5;
				}

				// We need to convert value in the 6th column which is a Object, into a double.
				// That value is not from the CSV file, it was added in the class TableTrading
				Object rawColumn6 = modelTrading.getValueAt(threadLocation, 6);
				int column6 =  (int)rawColumn6;

				if (column3.equals("N/A")){
					// If there is no bid price, selling value is always 0
					column9 = "0,00 €";
				}
				else {
					bidPrice = Double.parseDouble(column3);	
					column3 = format.format(bidPrice);

					double sellingValueRaw = bidPrice * column6;
					column9 = format.format(sellingValueRaw);
				}

				if (column4.equals("N/A")){
					// If there is no ask price, buying value is always 0
					column10 = "0,00 €";
				}
				else {
					askPrice = Double.parseDouble(column4);	
					column4 = format.format(askPrice);

					double buyingValueRaw = askPrice * column6;
					column10 = format.format(buyingValueRaw);
				}

				// This will set a particular value for particular row and column
				modelTrading.setValueAt(column0, threadLocation,0);
				modelTrading.setValueAt(column1,threadLocation,1);
				modelTrading.setValueAt(column2,threadLocation,2);
				modelTrading.setValueAt(column3,threadLocation,3);
				modelTrading.setValueAt(column4,threadLocation,4);
				modelTrading.setValueAt(column5,threadLocation,5);
				// Column number 6 is the quantity and it will be set in the class TableTrading
				modelTrading.setValueAt("Sell",threadLocation,7);
				modelTrading.setValueAt("Buy",threadLocation,8);
				modelTrading.setValueAt(column9,threadLocation,9);
				modelTrading.setValueAt(column10,threadLocation,10);
			}

			catch(InterruptedException | IOException exc) {
				// If you see this message, that is because there is short-term problem with Yahoo's server 
				// and not with this program: 
				// "Server returned HTTP response code: 999 for URL: http://download.finance.yahoo.com/d/quotes.csv?s=BK&f=nsxbap2"
				System.out.println(exc);
			}
		}
	}
}