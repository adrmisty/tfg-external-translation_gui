package main.java.gui.cards;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import main.java.logic.util.exception.ResourceException;
import main.java.logic.util.properties.ResourceLoader;

public class CardMode extends JPanel {

    private static final long serialVersionUID = 1L;
    private MainWindow root;

    /*
     * Panels
     */
    private JPanel northPanel_Mode;
    private JPanel downPanel_Mode;
    private JPanel centerPanel_Mode;

    /*
     * Labels & text
     */
    private JButton btnManual_Mode;
    private JButton btnAutomatic_Mode;
    private JLabel lblChooseTranslation;
    private JLabel lblChoose;
    private JLabel lblLanguage;
    private JPanel backEmptyPanel_Mode;
    private JPanel backPanel_Mode;
    private JLabel lblBack_Mode;
    private JButton btnBack_Mode;
    private JButton btnNext_Mode;
    private JButton btnHelp_Mode;

    /*
     * Selected languages
     */
    private JComboBox<JCheckBox> comboBox;
    private CheckComboBoxRenderer renderer;
    private List<String> selectedLanguages;
    private static int maxLangs = 3;

    public CardMode(MainWindow root) throws ResourceException {
	this.root = root;

	this.setBackground(SystemColor.window);
	this.setLayout(null);
	this.add(getNorthPanel_Mode());
	this.add(getCenterPanel_Mode());
	this.add(getDownPanel_Mode());
    }

    private JPanel getNorthPanel_Mode() throws ResourceException {
	if (northPanel_Mode == null) {
	    northPanel_Mode = new JPanel();
	    northPanel_Mode.setBounds(0, 0, 586, 81);
	    northPanel_Mode.setBackground(SystemColor.window);
	    northPanel_Mode.setLayout(null);
	    northPanel_Mode.add(getLblChooseTranslation());
	}
	return northPanel_Mode;
    }

    private JPanel getDownPanel_Mode() throws ResourceException {
	if (downPanel_Mode == null) {
	    downPanel_Mode = new JPanel();
	    downPanel_Mode.setBounds(0, 315, 586, 98);
	    downPanel_Mode.setLayout(new GridLayout(0, 1, 0, 0));
	    downPanel_Mode.add(getBackEmptyPanel_Mode());
	    downPanel_Mode.add(getBackPanel_Mode());
	}
	return downPanel_Mode;
    }

    private JPanel getCenterPanel_Mode() throws ResourceException {
	if (centerPanel_Mode == null) {
	    centerPanel_Mode = new JPanel();
	    centerPanel_Mode.setBounds(0, 80, 586, 236);
	    centerPanel_Mode.setBackground(SystemColor.window);
	    centerPanel_Mode.setLayout(null);
	    centerPanel_Mode.add(getBtnManual_Mode());
	    centerPanel_Mode.add(getBtnAutomatic_Mode());
	    centerPanel_Mode.add(getComboBox());
	    centerPanel_Mode.add(getLblChoose());
	    centerPanel_Mode.add(getLblLanguage());
	}
	return centerPanel_Mode;
    }

    private void setSelectedButton(JButton thisButton, JButton otherButton) {
	thisButton.setSelected(true);
	thisButton.setFocusable(true);
	otherButton.setSelected(false);
	otherButton.setFocusable(false);
    }

    private boolean unlockNext() {
	// Checks whether it is possible to enable the 'Next' button
	if ((btnManual_Mode.isSelected() || btnAutomatic_Mode.isSelected())
		&& comboBox.getSelectedIndex() > 0) {
	    btnNext_Mode.setEnabled(true);
	    return true;
	} else {
	    btnNext_Mode.setEnabled(false);
	    return false;
	}
    }

    private JButton getBtnManual_Mode() throws ResourceException {
	if (btnManual_Mode == null) {
	    btnManual_Mode = new JButton(
		    root.getMessages().getString("button.mode.manual"));
	    btnManual_Mode.setFocusable(false);
	    btnManual_Mode.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    setSelectedButton(btnManual_Mode, btnAutomatic_Mode);
		    maxLangs = 1;
		    unlockNext();
		}
	    });
	    btnManual_Mode.setBounds(67, 77, 210, 52);
	    btnManual_Mode.setFont(ResourceLoader.getFont().deriveFont(20f));
	}
	return btnManual_Mode;
    }

    private JButton getBtnAutomatic_Mode() throws ResourceException {
	if (btnAutomatic_Mode == null) {
	    btnAutomatic_Mode = new JButton(
		    root.getMessages().getString("button.mode.auto"));
	    btnAutomatic_Mode.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    setSelectedButton(btnAutomatic_Mode, btnManual_Mode);
		    maxLangs = 3;
		    try {
			root.setMode(null);
		    } catch (Exception e1) {
			root.showErrorMessage(e1.getMessage());
		    }
		    unlockNext();
		}
	    });
	    btnAutomatic_Mode.setFont(ResourceLoader.getFont().deriveFont(20f));
	    btnAutomatic_Mode.setBounds(305, 77, 210, 52);
	}
	return btnAutomatic_Mode;
    }

    private JComboBox<JCheckBox> getComboBox() throws ResourceException {
	if (comboBox == null) {
	    comboBox = new JComboBox<JCheckBox>();
	    renderer = new CheckComboBoxRenderer();
	    comboBox.setRenderer(renderer);
	    comboBox.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {

		    comboBox.getItemAt(comboBox.getSelectedIndex())
			    .setSelected(!renderer.isSelected());
		    if (getSelectedCount() > maxLangs) {
			comboBox.getItemAt(comboBox.getSelectedIndex())
				.setSelected(false);
		    }
		    if (btnNext_Mode != null) {
			unlockNext();
		    }
		}
	    });
	    comboBox.setBounds(158, 185, 280, 32);
	    comboBox.setFont(ResourceLoader.getFont().deriveFont(15f));
	    sortComboBox();
	}
	return comboBox;
    }

    private int getSelectedCount() {
	// Do not count first item
	this.selectedLanguages = new ArrayList<>();
	int count = 0;
	for (int i = 1; i < comboBox.getItemCount(); i++) {
	    JCheckBox language = comboBox.getItemAt(i);
	    if (language.isSelected()) {
		this.selectedLanguages.add(language.getText());
		count++;
	    }
	}
	return count;
    }

    /**
     * Sorts all the language in the combo box alphabetically, while having the
     * first element always being the same.
     */
    private void sortComboBox() {
	// Add all elements to combo box
	List<String> languages = ResourceLoader
		.getSupportedLanguages(root.getMessages());
	JCheckBox first = new JCheckBox(languages.get(0));

	for (int i = 1; i < comboBox.getItemCount(); i++) {
	    comboBox.addItem(comboBox.getItemAt(i));
	}

	Collections.sort(languages);
	comboBox.removeAllItems();

	first.setEnabled(false);
	first.setSelected(false);
	comboBox.addItem(first);

	for (String s : languages) {
	    if (!s.equals(first.getText())) {
		comboBox.addItem(new JCheckBox(s));
	    }
	}
    }

    private JLabel getLblChoose() throws ResourceException {
	if (lblChoose == null) {
	    lblChoose = new JLabel(
		    root.getMessages().getString("label.mode.type"));
	    lblChoose.setForeground(SystemColor.textHighlight);
	    lblChoose.setHorizontalAlignment(SwingConstants.CENTER);
	    lblChoose.setBounds(67, 44, 448, 22);
	    lblChoose.setFont(ResourceLoader.getFont().deriveFont(15f));
	}
	return lblChoose;
    }

    private JLabel getLblLanguage() throws ResourceException {
	if (lblLanguage == null) {
	    lblLanguage = new JLabel(
		    root.getMessages().getString("label.mode.language"));
	    lblLanguage.setLabelFor(getComboBox());
	    lblLanguage.setHorizontalAlignment(SwingConstants.CENTER);
	    lblLanguage.setForeground(SystemColor.textHighlight);
	    lblLanguage.setFont(ResourceLoader.getFont().deriveFont(15f));
	    lblLanguage.setBounds(67, 155, 448, 19);
	}
	return lblLanguage;
    }

    private JLabel getLblChooseTranslation() throws ResourceException {
	if (lblChooseTranslation == null) {
	    lblChooseTranslation = new JLabel(
		    root.getMessages().getString("label.mode.title"));
	    lblChooseTranslation.setBounds(0, 0, 586, 81);
	    lblChooseTranslation.setHorizontalAlignment(SwingConstants.CENTER);
	    lblChooseTranslation
		    .setFont(ResourceLoader.getFont().deriveFont(40f));
	}
	return lblChooseTranslation;
    }

    private JPanel getBackEmptyPanel_Mode() {
	if (backEmptyPanel_Mode == null) {
	    backEmptyPanel_Mode = new JPanel();
	    backEmptyPanel_Mode.setBackground(SystemColor.window);
	}
	return backEmptyPanel_Mode;
    }

    private JPanel getBackPanel_Mode() throws ResourceException {
	if (backPanel_Mode == null) {
	    backPanel_Mode = new JPanel();
	    backPanel_Mode.setBackground(SystemColor.window);
	    backPanel_Mode.setLayout(null);
	    backPanel_Mode.add(getLblBack_Mode());
	    backPanel_Mode.add(getBtnBack_Mode());
	    backPanel_Mode.add(getBtnNext_Mode());
	    backPanel_Mode.add(getBtnHelp_Mode());
	}
	return backPanel_Mode;
    }

    private JLabel getLblBack_Mode() throws ResourceException {
	if (lblBack_Mode == null) {
	    lblBack_Mode = new JLabel("<");
	    lblBack_Mode.setLabelFor(getBtnBack_Mode());
	    lblBack_Mode.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    btnBack_Mode.doClick();
		}
	    });
	    lblBack_Mode.setBounds(10, 11, 31, 37);
	    lblBack_Mode.setFont(ResourceLoader.getFont().deriveFont(15f));
	}
	return lblBack_Mode;
    }

    private JButton getBtnBack_Mode() {
	if (btnBack_Mode == null) {
	    btnBack_Mode = new JButton("");
	    btnBack_Mode.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    try {
			root.show("file");
		    } catch (Exception e1) {
			root.showErrorMessage(e1.getMessage());
		    }
		}
	    });
	    btnBack_Mode.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/img/home-icon.png")));
	    btnBack_Mode.setBounds(20, 11, 31, 37);
	    btnBack_Mode.setMnemonic('b');
	    btnBack_Mode.setBorder(null);
	    btnBack_Mode.setBackground(SystemColor.window);
	}
	return btnBack_Mode;
    }

    private JButton getBtnNext_Mode() throws ResourceException {
	if (btnNext_Mode == null) {
	    btnNext_Mode = new JButton(
		    root.getMessages().getString("button.next"));
	    btnNext_Mode.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    root.setLanguages(selectedLanguages);
		    if (btnAutomatic_Mode.isSelected()) {
			try {
			    root.show("image");
			} catch (Exception e1) {
			    root.showErrorMessage(e1.getMessage());
			}
		    } else {
			try {
			    root.show("manual");
			} catch (Exception e1) {
			    root.showErrorMessage(e1.getMessage());
			}
		    }
		}
	    });
	    btnNext_Mode.setBounds(421, 11, 115, 35);
	    btnNext_Mode.setMnemonic('n');
	    btnNext_Mode.setFont(ResourceLoader.getFont().deriveFont(14f));
	    btnNext_Mode.setEnabled(false);
	}
	return btnNext_Mode;
    }

    private JButton getBtnHelp_Mode() throws ResourceException {
	if (btnHelp_Mode == null) {
	    btnHelp_Mode = new JButton("");
	    btnHelp_Mode.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/img/help.png")));
	    btnHelp_Mode.setBounds(537, 7, 49, 41);
	    btnHelp_Mode.setToolTipText(
		    root.getMessages().getString("tooltip.mode"));
	    btnHelp_Mode.setFont(ResourceLoader.getFont().deriveFont(14f));
	    btnHelp_Mode.setBorder(null);
	    btnHelp_Mode.setBackground(SystemColor.window);
	    btnHelp_Mode.setFocusable(false);
	}
	return btnHelp_Mode;
    }

    public void reset() {
	comboBox.setSelectedIndex(0);
	btnManual_Mode.setSelected(false);
	btnAutomatic_Mode.setSelected(false);
    }

    /*
     * COMBO BOX
     */

    // Renderer to display checkboxes in the combo box
    class CheckComboBoxRenderer implements ListCellRenderer<Object> {

	JCheckBox checkBox = new JCheckBox();

	@Override
	public Component getListCellRendererComponent(JList<?> list,
		Object value, int index, boolean isSelected,
		boolean cellHasFocus) {
	    checkBox = (JCheckBox) value;
	    checkBox.setSelected(((JCheckBox) value).isSelected());
	    if (isSelected) {
		checkBox.setBackground(list.getSelectionBackground());
		checkBox.setForeground(list.getSelectionForeground());
	    } else {
		checkBox.setBackground(list.getBackground());
		checkBox.setForeground(list.getForeground());
	    }

	    return checkBox;
	}

	public boolean isSelected() {
	    return checkBox.isSelected();
	}

	public void select(boolean sel) {
	    checkBox.setSelected(sel);
	}
    }

}
