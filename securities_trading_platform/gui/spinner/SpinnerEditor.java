package securities_trading_platform.gui.spinner;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/*
 * This class is used to set the spinner for the particular column.
 * I have found this code somewhere on the Internet.
 * Credit to the author, I forgot his/her name (that is not nice).
 */
public class SpinnerEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 1L;
	JSpinner spinner;
	JSpinner.DefaultEditor editor;
	JTextField textField;
	boolean valueSet;

	// Initializes the spinner.
	public SpinnerEditor() {
		super(new JTextField());
		// For quantity, which is column no. 7:
		// 0 is the initial value, 0 is the minimum value, 999.999.999 maximum and 1 is step
		SpinnerModel model = new SpinnerNumberModel(0, 0, 999999999, 1);
		spinner = new JSpinner(model);
		editor = ((JSpinner.DefaultEditor)spinner.getEditor());
		textField = editor.getTextField();
		textField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent fe) {
				if (valueSet) {
					textField.setCaretPosition(1);
				}
			}
			public void focusLost(FocusEvent fe) {
			}
		});
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				stopCellEditing();
			}
		});
	}

	// Prepares the spinner component and returns it.
	public Component getTableCellEditorComponent(
			JTable table, Object value, boolean isSelected, int row, int column) {
		if (!valueSet) {
			spinner.setValue(value);
		}
		textField.requestFocus();
		return spinner;
	}

	public boolean isCellEditable(EventObject eo) {
		if (eo instanceof KeyEvent) {
			KeyEvent ke = (KeyEvent)eo;
			System.err.println("key event: "+ke.getKeyChar());
			textField.setText(String.valueOf(ke.getKeyChar()));
			valueSet = true;
		} else {
			valueSet = false;
		}
		return true;
	}

	// Returns the spinners current value.
	public Object getCellEditorValue() {
		return spinner.getValue();
	}

	public boolean stopCellEditing() {
		try {
			editor.commitEdit();
			spinner.commitEdit();
		} catch (java.text.ParseException e) {
			JOptionPane.showMessageDialog(null, "Invalid quantity. Quantity must be the whole and positive number <= 1.000.000.");
		}
		return super.stopCellEditing();
	}
}