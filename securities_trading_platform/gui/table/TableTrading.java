package securities_trading_platform.gui.table;

import javax.swing.table.DefaultTableModel;

/*
 * Purpose of this class is to override DefaultTableModel because
 * we need to prevent user from editing table cells.
 */
public class TableTrading extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	// This is an array of stock tickers or stock symbols (these are like initials for the companies)
	public static String[] rowNames = {"SIE.DE","DAIC.DE","TIT.MI","UN.AS","EBS.VI","AIR.PA","EXO.MI","VIG.VI","DTE.DE","ML.PA","UCG.MI","EN.PA",
			"CA.PA", "OMV.VI","BMW.DE","WIE.VI","LUX.MI","ADS.DE","SAP.DE","HEIO.AS"};

	public static int rowNamesLength = rowNames.length;

	public TableTrading() {

		String[] columnNames = {"Corporation", "Ticker", "Exchange", "Bid", "Ask", "Change", "Quantity", 
				"", "", "Selling value", "Buying value"};

		for (int i = 0; i < columnNames.length; i++){
			addColumn(columnNames[i]);
		}

		for (int i = 0; i < rowNamesLength; i++) {
			// The following declaration is very important because the method addRow(Object[] rowData)
			// will first be filled with null, then later, it will be filled with new values by the method
			// public void setValueAt(Object aValue, int row, int column) which is used in the class 
			// MyRunnable
			Object[] rowData = null;
			
			addRow(rowData);
			// Will "manually" set value for the quantity which is 6th column
			setValueAt(0,i,6);
		}
	}

	/*
	 * This method prevents certain columns to be editable
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		if (col == 6) 			// This is important if you want to be able to enter quantity
			return true;
		else if (col == 7)		// This is important if you want to be able to press the sell button
			return true;
		else if (col == 8)		// This is important if you want to be able to press the buy button
			return true;	
		else 
			return false;
	}
}