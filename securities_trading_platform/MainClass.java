package securities_trading_platform;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import securities_trading_platform.gui.button.ButtonEditor;
import securities_trading_platform.gui.button.ButtonRenderer;
import securities_trading_platform.gui.column.ColumnBackgroundRenderer;
import securities_trading_platform.gui.column.CustomTableRenderer;
import securities_trading_platform.gui.spinner.SpinnerEditor;
import securities_trading_platform.gui.table.TablePortfolio;
import securities_trading_platform.gui.table.TableTrading;
import securities_trading_platform.gui.table.TableTransactions;

/*
 * This class creates 3 tables on the event-dispatching thread.
 * 
 * I haven't used a SwingWorker class (for the JTable tableTrading) as a background thread 
 * because it can be instantiated, at the same time, only 10 times maximum (for that table 
 * I need more instances). BTW this is not mentioned in the documentation (On 9th July 
 * I have reported this bug).  
 * 
 * @author       Zoran Bosanac
 * @version      1, 29. May 2016.
 *
 * Important note: starting on 22nd of July, problems with the Yahoo server became very often. 
 * At the moment there are no other sources of stock data for free (Google finance doesn't have 
 * it and to get stock data directly from the stock markets, Reuters or Bloomberg - that would 
 * be costly endeavor). Because of that, this program sometimes is not fully operational. 
 * Please accept my apology, but at the moment I don't have any other option.
 */
public class MainClass {

	public static TableTrading modelTrading = new TableTrading();
	public static JTable tableTrading = new JTable(modelTrading);

	public static Font font = new Font(null, Font.PLAIN, 15);

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// We can run the following method, but it is not necessary
		// System.out.println("Created GUI on EDT? "+
		//		javax.swing.SwingUtilities.isEventDispatchThread());

		TablePortfolio modelPortfolio = new TablePortfolio();
		TableTransactions modelTransactions = new TableTransactions();

		JTable tablePortfolio = new JTable(modelPortfolio);
		JTable tableTransactions = new JTable(modelTransactions);

		// This method enables rows sorting. For the rows in which are numbers, it doesn't work well,
		// so this should be improved in the next version
		tableTrading.setAutoCreateRowSorter(true);
		tablePortfolio.setAutoCreateRowSorter(true);
		tableTransactions.setAutoCreateRowSorter(true);

		// Will set font for all cells in table except column names
		tableTrading.setFont(font);
		tablePortfolio.setFont(font);
		tableTransactions.setFont(font);

		// Will set font only for all column names
		tableTrading.getTableHeader().setFont(font);
		tablePortfolio.getTableHeader().setFont(font);
		tableTransactions.getTableHeader().setFont(font);

		// Will prevent table to resize column width
		tableTrading.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablePortfolio.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableTransactions.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// Will set width of a particular column
		tableTrading.getColumnModel().getColumn(0).setPreferredWidth(200);
		tableTrading.getColumnModel().getColumn(1).setPreferredWidth(80);
		tableTrading.getColumnModel().getColumn(2).setPreferredWidth(100);
		tableTrading.getColumnModel().getColumn(3).setPreferredWidth(90);
		tableTrading.getColumnModel().getColumn(4).setPreferredWidth(90);
		tableTrading.getColumnModel().getColumn(5).setPreferredWidth(80);
		tableTrading.getColumnModel().getColumn(6).setPreferredWidth(100);
		tableTrading.getColumnModel().getColumn(7).setPreferredWidth(80);
		tableTrading.getColumnModel().getColumn(8).setPreferredWidth(80);
		tableTrading.getColumnModel().getColumn(9).setPreferredWidth(130);
		tableTrading.getColumnModel().getColumn(10).setPreferredWidth(130);
		tablePortfolio.getColumnModel().getColumn(0).setPreferredWidth(200);
		tablePortfolio.getColumnModel().getColumn(1).setPreferredWidth(80);
		tablePortfolio.getColumnModel().getColumn(2).setPreferredWidth(100);
		tablePortfolio.getColumnModel().getColumn(3).setPreferredWidth(100);
		tablePortfolio.getColumnModel().getColumn(4).setPreferredWidth(130);
		tableTransactions.getColumnModel().getColumn(0).setPreferredWidth(200);
		tableTransactions.getColumnModel().getColumn(1).setPreferredWidth(80);
		tableTransactions.getColumnModel().getColumn(2).setPreferredWidth(100);
		tableTransactions.getColumnModel().getColumn(3).setPreferredWidth(100);
		tableTransactions.getColumnModel().getColumn(4).setPreferredWidth(130);
		tableTransactions.getColumnModel().getColumn(5).setPreferredWidth(130);

		// Will set height of all rows in table
		tableTrading.setRowHeight(20);
		tablePortfolio.setRowHeight(20);
		tableTransactions.setRowHeight(20);

		// We need this to change text color of the 6th column
		tableTrading.getColumnModel().getColumn(5).setCellRenderer(new CustomTableRenderer());

		// This will set the background for the 7th column which is the only editable column (except buttons, which 
		// are 8th and 9th column)
		TableCellRenderer defaultRenderer = tableTrading.getDefaultRenderer(Object.class);
		TableCellRenderer rend = new ColumnBackgroundRenderer(defaultRenderer);
		tableTrading.setDefaultRenderer(Object.class, rend);

		// Will set spinner in the 7th column
		tableTrading.getColumnModel().getColumn(6).setCellEditor(new SpinnerEditor());

		// Will set button in the 8th column 
		// In the class TableTrading, method isCellEditable should return true for that column
		tableTrading.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
		tableTrading.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JCheckBox(), tableTrading, modelTransactions));
		// Will set button in the 9th column 
		// In the class TableTrading, method isCellEditable should return true for that column
		tableTrading.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
		tableTrading.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JCheckBox(), tableTrading, modelTransactions));

		// Create and set up the window
		JFrame frame = new JFrame("Securities trading platform");

		JLabel label1 = new JLabel("This is just a trading simulation, so you can freely click on any buy or sell button.");
		JLabel label2 = new JLabel("Settings: 1-click button. Meaning: user needs to just once press buy or sell button "
				+ "for the virtual transaction to be realized.");
		JLabel label3 = new JLabel("Minimum transaction is 1.000 stocks or value of 1.000,00 €. Maximum transaction is "
				+ "1.000.000 stocks or value of 10.000.000,00 €.");
		JLabel label4 = new JLabel("Short selling is not allowed. Meaning: for example if you have 0 stocks of Siemens "
				+ "in your portfolio, then you can't sell Siemens, first you need to buy it.");
		JLabel label5 = new JLabel("Often, there is a problem with the Yahoo's server. Thats the reason why buying or "
				+ "selling value is calculated so slowly (because new price is lagging), or even the whole row is empty for a "
				+ "few seconds (because data is lagging");
		JLabel label6 = new JLabel("Info: every transaction will be saved in the database (Postgres, local) and in the tables: "
				+ "portfolio and transactions.");
		JLabel label7 = new JLabel("Just an info: trading hours for most of the major European stock markets are between 09:00 "
				+ "- 17:30 CET. Yahoo finance publishes data with 15 minutes delay.");
		JLabel label8 = new JLabel("This is just a small piece of the real securities trading platform. It can be further upgraded.");

		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.add(label1);
		panel.add(label2);
		panel.add(label3);
		panel.add(label4);
		panel.add(label5);
		panel.add(label6);
		panel.add(label7);
		panel.add(label8);

		frame.add(panel, BorderLayout.SOUTH);

		// This is for the tabs
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Trading", new JScrollPane(tableTrading));
		tabbedPane.addTab("Portfolio", new JScrollPane(tablePortfolio));
		tabbedPane.addTab("Transactions", new JScrollPane(tableTransactions));

		frame.add(tabbedPane, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Sets this window to the full screen size
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);

		// Display the window
		frame.pack();
		frame.setVisible(true);

		MySwingWorker sw = new MySwingWorker(modelPortfolio);
		sw.execute();
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

		for (int i = 0; i < TableTrading.rowNamesLength; i++) {
			// Create and start threads
			new Thread(new MyRunnable(TableTrading.rowNames[i], i, modelTrading)).start();
		}
	}
}