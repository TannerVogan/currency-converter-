package currency;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JTextField;
import org.junit.Before;
import org.junit.Test;

public class CurrencyConverterTest {

	private CurrencyConverter currConv;
	private JLabel toLabel;
	private JLabel fromLabel;
	private JTextField fromField;
	private JTextField toField;
	private JComboBox fromCombo;
	private JComboBox toCombo;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		currConv = new CurrencyConverter();
		fromField = (JTextField) getNamedComponent(currConv, "fromField");
		toField = (JTextField) getNamedComponent(currConv, "toField");
		fromLabel = (JLabel) getNamedComponent(currConv, "fromLabel");
		toLabel = (JLabel) getNamedComponent(currConv, "toLabel");
		fromCombo = (JComboBox) getNamedComponent(currConv, "fromCombo");
		toCombo = (JComboBox) getNamedComponent(currConv, "toCombo");
		assertNotNull(fromField);
		assertNotNull(toField);
		assertNotNull(fromCombo);
		assertNotNull(toCombo);
	}

	@Test
	public void testBasic() {
		fromCombo.setSelectedItem("US Dollar");
		toCombo.setSelectedItem("Euro");
		fromField.setText("2");
		fromField.postActionEvent();

		assertEquals(fromLabel.getText(), "2 US Dollar equals");
		assertEquals(toLabel.getText(), "1.76 Euro");
		assertEquals("2", fromField.getText());
		assertEquals("1.76", toField.getText());
		assertEquals("US Dollar", fromCombo.getSelectedItem());
		assertEquals("Euro", toCombo.getSelectedItem());
	}

	@Test
	public void testFromField() {
		fromField.setText("hello");
		fromField.postActionEvent();

		assertEquals("ERR:NON_NUM", toField.getText());

		fromField.setText("-47");
		fromField.postActionEvent();

		assertEquals("ERR:NEG_NUM", toField.getText());

		fromField.setText("0");
		fromField.postActionEvent();

		assertEquals("0", toField.getText());
		
		fromField.setText("0.1111");
		fromField.postActionEvent();
		assertEquals("0.11", fromField.getText());
	}
	
	@Test
	public void testToField(){
		toCombo.setSelectedItem("Euro");
		fromCombo.setSelectedItem("British Pound");
		
		fromField.setText("9123123123.88");
		fromField.postActionEvent();
		assertEquals("ERR:MAX_NUM", toField.getText());
		
		fromField.setText("0.11");
		fromField.postActionEvent();
		assertEquals(fromLabel.getText(), "0.11 British Pound equals");
		assertEquals(toLabel.getText(), "0.14 Euro");
		assertEquals("0.14", toField.getText());
		
		toCombo.setSelectedItem("Japanese Yen");
		fromField.setText("0.01");
		fromField.postActionEvent();
		assertEquals(fromLabel.getText(), "0.01 British Pound equals");
		assertEquals(toLabel.getText(), "1.55 Japanese Yen");
		assertEquals("1.55", toField.getText());
		
		toCombo.setSelectedItem("British Pound");
		fromCombo.setSelectedItem("Japanese Yen");
		assertEquals(fromLabel.getText(), "0.01 Japanese Yen equals");
		assertEquals(toLabel.getText(), "0.000064 British Pound");
		assertEquals("0.000064", toField.getText());
		
	}

	@Test
	public void testSwap() {
		toField.setText("1");
		toField.postActionEvent();
		
		fromCombo.setSelectedItem("British Pound");
		toCombo.setSelectedItem("Brazilian Real");

		fromCombo.setSelectedItem("Brazilian Real");
		assertEquals(toCombo.getSelectedItem(), "British Pound");
		assertEquals(fromLabel.getText(), "1 Brazilian Real equals");
		assertEquals(toLabel.getText(), "0.19 British Pound");
		
		toCombo.setSelectedItem("Brazilian Real");
		assertEquals(fromCombo.getSelectedItem(), "British Pound");
		assertEquals(fromLabel.getText(), "1 British Pound equals");
		assertEquals(toLabel.getText(), "5.16 Brazilian Real");
	}

	// ==========================================================
	// GetNamedComponent Method
	// ==========================================================

	/*
	 * From gov.final.ichiro.test.TestUtils.java See "Automate GUI tests for
	 * Swing applications: Transition from unit tests to acceptance tests" by
	 * Ichiro Suzuki in JavaWorld, November 15, 2004
	 */
	private Component getNamedComponent(Component parent, String name) {
		if (name.equals(parent.getName())) {
			return parent;
		}
		if (parent instanceof Container) {
			Component[] children = (parent instanceof JMenu) ? ((JMenu) parent).getMenuComponents()
					: ((Container) parent).getComponents();
			for (Component child : children) {
				Component result = getNamedComponent(child, name);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}
}
