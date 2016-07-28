package securities_trading_platform.gui.button;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;

import securities_trading_platform.databaseActions.BuyAction;
import securities_trading_platform.databaseActions.SellAction;
import securities_trading_platform.gui.table.TableTransactions;

/*
 * This class is used to add button to the cell in a JTable tableTrading. 
 * On a background thread, SwingWorker, it also sends data 
 * from JTable tableTrading (when button is clicked), to the Postgres (local) 
 * database (through classes BuyAction or SellAction).
 */
public class ButtonEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 1L;
	private JButton button;
	private String label;
	@SuppressWarnings("unused")
	private boolean isPushed;
	private JTable tableTrading;
	TableTransactions modelTransactions;

	public ButtonEditor(JCheckBox checkBox, JTable tableTrading, TableTransactions modelTransactions) {
		super(checkBox);
		this.tableTrading = tableTrading;
		this.modelTransactions = modelTransactions;
		
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Makes the renderer reappear
				fireEditingStopped(); 

				// Creates and executes background thread - SwingWorker.
				// The Void class is an uninstantiable placeholder class to hold a 
				// reference to the Class object representing the Java keyword void.
				new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						updatesDatabase();
						return null;
					}
				}.execute();
			}
		});
	}

	/*
	 * Sends data from the pressed row into the database (through classes BuyAction or SellAction)
	 */
	void updatesDatabase() {

		// This will save number of a row that is pressed by the button
		int selectedRow = tableTrading.getSelectedRow();

		// We need to extract data from the TableTrading
		Object nameRaw = tableTrading.getValueAt(selectedRow, 0);
		String name_of_the_corporation = (String)nameRaw;
		Object symbolRaw = tableTrading.getValueAt(selectedRow, 1);
		String trading_symbol = (String)symbolRaw;
		Object exchangeRaw = tableTrading.getValueAt(selectedRow, 2);
		String stock_exchange = (String)exchangeRaw;
		Object quantityRaw1 = tableTrading.getValueAt(selectedRow, 6);
		int quantity = (int)quantityRaw1;

		// Will convert object for example xxx.xxx.xxx,xx € into double xxxxxxxxx.xx
		Object sellingValueRaw1 = tableTrading.getValueAt(selectedRow, 9);
		String sellingValueRaw2 = (String)sellingValueRaw1;
		String sellingValueRaw3 = sellingValueRaw2.substring(0, sellingValueRaw2.length() - 2);
		String sellingValueRaw4 = sellingValueRaw3.replaceAll("\\.", "");
		String sellingValueRaw5 = sellingValueRaw4.replaceAll(",", ".");
		double selling_value = Double.parseDouble(sellingValueRaw5);

		// Will convert object for example xxx.xxx.xxx,xx € into double xxxxxxxxx.xx
		Object buyingValueRaw1 = tableTrading.getValueAt(selectedRow, 10);
		String buyingValueRaw2 = (String)buyingValueRaw1;
		String buyingValueRaw3 = buyingValueRaw2.substring(0, buyingValueRaw2.length() - 2);
		String buyingValueRaw4 = buyingValueRaw3.replaceAll("\\.", "");
		String buyingValueRaw5 = buyingValueRaw4.replaceAll(",", ".");
		double buying_value = Double.parseDouble(buyingValueRaw5);

		if (quantity < 1000 || quantity > 1000000) {
			JOptionPane.showMessageDialog(null, "You can't trade with less then 1.000 or more then 1.000.000 stocks. Please "
					+ "change the quantity.");
		}
		else {
			if (buying_value > 10000000 || selling_value > 10000000) {
				JOptionPane.showMessageDialog(null, "Transaction can't be larger then 10.000.000,00 €. Please reduce the quantity.");
			}
			else if (buying_value == 0 || selling_value == 0) {
				// Calculation can be slow because problems with the Yahoo's server are often
				JOptionPane.showMessageDialog(null, "Please wait for value to be calculated.");	
			}			
			else if ((buying_value < 1000 && buying_value != 0) || (selling_value < 1000 && selling_value != 0)) {
				JOptionPane.showMessageDialog(null, "Transaction can't be smaller then 1.000,00 €. Please increase the quantity.");
			}	
			else if ((buying_value <= 10000000 && buying_value >= 1000) || (selling_value <= 10000000 && selling_value >= 1000)) {

				// This will save number of a column that is pressed by the button
				int selectedColumn = tableTrading.getSelectedColumn();

				// If sell button is pressed
				if (selectedColumn == 7) {

					// This will send extracted data to the class SellAction
					new SellAction(name_of_the_corporation, trading_symbol, stock_exchange, quantity, selling_value, modelTransactions);
				}

				// If buy button is pressed
				if (selectedColumn == 8) {

					// This will send extracted data to the class BuyAction
					new BuyAction(name_of_the_corporation, trading_symbol, stock_exchange, quantity, buying_value);

					// Will format quantity into ###.###.###
					String quantityFormatted = NumberFormat.getNumberInstance(Locale.GERMANY).format(quantity);

					// Will be needed for formatting buying_value
					java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.GERMANY);

					// Will format buying_value into ###.###.###,## €
					String buying_valueFormatted = format.format(buying_value);

					Object[] buyButtonPressedTransactions = {name_of_the_corporation, trading_symbol, stock_exchange, quantityFormatted,
							"", buying_valueFormatted};

					// Adds row in table TableTransactions
					modelTransactions.addRow(buyButtonPressedTransactions);
				}
			}
		}
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		if (isSelected) {
			button.setForeground(table.getSelectionForeground());
			button.setBackground(table.getSelectionBackground());
		} else {
			button.setForeground(table.getForeground());
			button.setBackground(table.getBackground());
		}
		label = (value == null) ? "" : value.toString();
		button.setText(label);
		isPushed = true;
		return button;
	}

	public Object getCellEditorValue() {
		isPushed = false;
		return new String(label);
	}

	public boolean stopCellEditing() {
		isPushed = false;
		return super.stopCellEditing();
	}

	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}
}