package securities_trading_platform.gui.column;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/*
 * This class is used to set text color for the particular column
 */
public class CustomTableRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		Component c = super.getTableCellRendererComponent(table, value, isSelected,
				hasFocus, row, column);

		String changeRaw = (String)value;

		if (changeRaw != null) {
			if (changeRaw.equals("Unknown") || changeRaw.equals("N/A")) {
				c.setForeground(Color.BLACK);
			}
			else {
				// We don't need % 
				String change = changeRaw.substring(0,5);
				// Convert from object String to primitive double 
				double changeInPercent = Double.parseDouble(change);	

				// To create new color use: https://en.wikipedia.org/wiki/ANSI_escape_code
				// You can also use program Paint
				Color myNewGreen = new Color (0, 170, 0);

				if (changeInPercent > 0){
					c.setForeground(myNewGreen);
				}
				else if (changeInPercent < 0){
					c.setForeground(Color.RED);
				}
				else 
					c.setForeground(Color.BLACK);
			}
		}
		return c;
	}
}