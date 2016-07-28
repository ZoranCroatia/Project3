package securities_trading_platform.gui.column;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/*
 * This class is used to set the background for the entire 6th column
 * 
 * This class is from: http://www.jyloo.com/news/?pubId=1282737395000
 * Credit to the unknown author from the company: Jyloo Software GmbH 
 */
public class ColumnBackgroundRenderer implements TableCellRenderer {
	private TableCellRenderer delegate;
	private Color alternateColor; 

	public ColumnBackgroundRenderer(TableCellRenderer defaultRenderer) {
		this.delegate = defaultRenderer;
		alternateColor = UIManager.getColor("Table.alternateRowColor");
	}

	public Component getTableCellRendererComponent(JTable table, Object value, 
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component c = delegate.getTableCellRendererComponent(table, value, isSelected, 
				hasFocus, row, column);    
		if (!isSelected) {  
			int modelColumn = table.convertColumnIndexToModel(column);
			//respect alternating row background when resetting
			Color defaultBackground = (row % 2 == 0 && alternateColor != null) ? alternateColor : table.getBackground();
			c.setBackground(modelColumn == 6 ? new Color(0xFFF0E0) : defaultBackground);
		}  
		return c;
	}
}