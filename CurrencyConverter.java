package currency;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Tanner Vogan 
 * @version Apr 18, 2016
 * Virginia Tech 
 */
public class CurrencyConverter extends JPanel {

	// +--------------------------+
	// | fromLabel |
	// | toLabel |
	// | fromField fromCombo |
	// | toField toCombo |
	// +--------------------------+

	private JLabel fromLabel;
	private JLabel toLabel;
	private JTextField fromField;
	private JTextField toField;
	private JComboBox fromCombo;
	private JComboBox toCombo;

	private JPanel labelPanel; // container for from and to labels
	private JPanel fromPanel; // container for fromField and fromCombo
	private JPanel toPanel; // container for toField and toCombo

	private Map<String, Double> currencies;

	public CurrencyConverter() {

		currencies = new HashMap<String, Double>();
		currencies.put("British Pound", 0.7);
		currencies.put("Brazilian Real", 3.61);
		currencies.put("Chinese Yuan", 6.48);
		currencies.put("Euro", 0.88);
		currencies.put("Indian Rupee", 66.42);
		currencies.put("Japanese Yen", 108.83);
		currencies.put("Russian Ruble", 66.12);
		currencies.put("US Dollar", 1.0);

		// ======================================================
		// Set up the six main components and give them names
		// ======================================================

		fromLabel = new JLabel("1 US Dollar equals");
		fromLabel.setName("fromLabel");
		fromLabel.setForeground(Color.GRAY);
		String fontName = fromLabel.getFont().getFontName();
		int style = fromLabel.getFont().getStyle();
		int size = fromLabel.getFont().getSize() - 2;

		fromLabel.setFont(new Font(fontName, style, size));

		toLabel = new JLabel("0.88 Euro");
		toLabel.setName("toLabel");
		fontName = toLabel.getFont().getFontName();
		style = toLabel.getFont().getStyle();
		size = toLabel.getFont().getSize() + 4;

		toLabel.setFont(new Font(fontName, style, size));

		// NOTE: 10 is the number of columns. Setting this is
		// an easy way to keep the field the same size
		fromField = new JTextField("1", 10);
		fromField.setName("fromField");
		fromField.addActionListener(e -> fromFieldActionPerformed(e));

		toField = new JTextField("0.88", 10);
		toField.setName("toField");
		toField.setEditable(false);

		String[] fromComboItems = { "British Pound", "Brazilian Real", "Chinese Yuan", "Euro", "Indian Rupee",
				"Japanese Yen", "Russian Ruble", "US Dollar" };
		fromCombo = new JComboBox<>(fromComboItems);
		fromCombo.setName("fromCombo");
		fromCombo.setSelectedItem("US Dollar");
		fromCombo.addItemListener(ie -> proceedFromCombo(ie));

		String[] toComboItems = { "British Pound", "Brazilian Real", "Chinese Yuan", "Euro", "Indian Rupee",
				"Japanese Yen", "Russian Ruble", "US Dollar" };
		toCombo = new JComboBox<>(toComboItems);
		toCombo.setName("toCombo");
		toCombo.setSelectedItem("Euro");
		toCombo.addItemListener(ie -> proceedToCombo(ie));

		// ======================================================
		// Set up panels for grouping the components
		// ======================================================

		labelPanel = new JPanel();
		fromPanel = new JPanel();
		toPanel = new JPanel();

		// NOTE: PAGE_AXIS makes the contents go from top to bottom
		BoxLayout lableBoxLayout = new BoxLayout(labelPanel, BoxLayout.PAGE_AXIS);
		labelPanel.setLayout(lableBoxLayout);
		labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		labelPanel.setAlignmentX(LEFT_ALIGNMENT);
		labelPanel.add(fromLabel);
		labelPanel.add(toLabel);

		fromPanel.setAlignmentX(LEFT_ALIGNMENT);
		fromPanel.add(fromField);
		fromPanel.add(fromCombo);

		toPanel.setAlignmentX(LEFT_ALIGNMENT);
		toPanel.add(toField);
		toPanel.add(toCombo);

		// Adds the panels to the content pane (this)
		BoxLayout boxLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		this.setLayout(boxLayout);
		this.add(labelPanel);
		this.add(fromPanel);
		this.add(toPanel);
	}

	private void proceedFromCombo(ItemEvent ie) {

		if (ie.getStateChange() == ItemEvent.DESELECTED) {
			String previous = (String) ie.getItem();
			String current = (String) fromCombo.getSelectedItem();

			if (current.equals(toCombo.getSelectedItem())) {
				toCombo.setSelectedItem(previous);
			}
		}

		convert();
	}

	private void proceedToCombo(ItemEvent ie) {

		if (ie.getStateChange() == ItemEvent.DESELECTED) {
			String previous = (String) ie.getItem();
			String current = (String) toCombo.getSelectedItem();

			if (current.equals(fromCombo.getSelectedItem())) {
				fromCombo.setSelectedItem(previous);
			}
		}

		convert();
	}

	private void fromFieldActionPerformed(ActionEvent e) {
		convert();
	}

	// Simple starter code to convert US Dollars to Euro's.
	private void convert() {
		double fromAmount;
		double toAmount;

		String fromCurrency = (String) fromCombo.getSelectedItem();
		String toCurrency = (String) toCombo.getSelectedItem();

		try {
			fromAmount = Double.parseDouble(fromField.getText());
			NumberFormat nf = new DecimalFormat("##.##");
			String correctForm = nf.format(fromAmount);
			fromAmount = Double.valueOf(correctForm);
			fromField.setText(correctForm);
			
		} catch (NumberFormatException e) {
			toField.setText("ERR:NON_NUM");
			return;
		}

		if (fromAmount < 0) {
			toField.setText("ERR:NEG_NUM");
			return;
		} else if (fromAmount == 0) {
			toField.setText("0");
			return;
		}

		double toValue = currencies.get(toCurrency);
		double fromValue = currencies.get(fromCurrency);

		toAmount = ((1 / fromValue) * toValue) * fromAmount;

		if (toAmount < 0.000001) {
			toField.setText("ERR:MIN_NUM");
			return;
		} else if (toAmount > 999999999.99) {
			toField.setText("ERR:MAX_NUM");
			return;
		}

		String toFieldText = "";

		if (toAmount >= 1)
			toFieldText = String.format("%.2f", toAmount);
		else if (toAmount < 1 && toAmount > 0) {
			double temp = toAmount;
			int zeros = 0;
			while (temp < 1) {
				temp *= 10;
				zeros++;
			}
			zeros += 1;

			if (zeros > 6)
				zeros = 6;
			toFieldText = String.format("%." + zeros + "f", toAmount);
		}
		toField.setText(toFieldText);
		NumberFormat nf = new DecimalFormat("##.##");
		fromLabel.setText(nf.format(fromAmount) + " " + fromCurrency + " equals");
		toLabel.setText(toFieldText + " " + toCurrency + "");

	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("CurrencyConverter");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JComponent newContentPane = new CurrencyConverter();
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
