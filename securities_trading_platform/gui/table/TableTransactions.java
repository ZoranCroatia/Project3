package securities_trading_platform.gui.table;

import javax.swing.table.DefaultTableModel;

/*
 * Purpose of this class is to override DefaultTableModel because
 * we need to prevent user from editing table cells.
 */
public class TableTransactions extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	public TableTransactions() {

		String[] columnNames = {"Corporation", "Ticker", "Exchange", "Quantity", "Selling value", "Buying value"};

		for (int i = 0; i < columnNames.length; i++){
			addColumn(columnNames[i]);
		}
	}

	/*
	 * This method prevents certain columns to be editable
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}
}