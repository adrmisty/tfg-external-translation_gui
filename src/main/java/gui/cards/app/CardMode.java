package main.java.gui.cards.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.java.gui.cards.MainWindow;
import main.java.gui.cards.help.HelpMode;
import main.java.util.exception.ResourceException;
import main.java.util.properties.ResourceLoader;

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
    private JPanel backPanel_Mode;
    private JLabel lblBack_Mode;
    private JButton btnBack_Mode;
    private JButton btnNext_Mode;
    private JButton btnHelp_Mode;

    /*
     * Selected languages
     */
    private JList<JCheckBox> languagesMenu;
    private List<String> languages = new ArrayList<>();
    private List<String> selectedLanguages = new ArrayList<>();
    private static int maxLangs = Integer.MAX_VALUE;
    private JScrollPane scrollPane;

    /*
     * Mnemonics
     */
    private KeyEventDispatcher keyEventDispatcher;

    public CardMode(MainWindow root) throws ResourceException {
	this.root = root;

	this.setBackground(SystemColor.window);
	this.setLayout(null);
	this.add(getDownPanel_Mode());
	this.add(getNorthPanel_Mode());
	this.add(getCenterPanel_Mode());
	btnAutomatic_Mode.doClick();
    }

    public void setKeyEventDispatcher(boolean set) {
	if (!set) {
	    KeyboardFocusManager.getCurrentKeyboardFocusManager()
		    .removeKeyEventDispatcher(this.keyEventDispatcher);
	} else {
	    if (this.keyEventDispatcher == null) {
		this.keyEventDispatcher = new KeyEventDispatcher() {
		    @Override
		    public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_F1
				&& e.getID() == KeyEvent.KEY_RELEASED) {
			    // Click help button
			    getBtnHelp_Mode().doClick();
			    return true; // consume the event
			}
			return false; // allow the event to be processed
				      // normally
		    }
		};
	    }
	    KeyboardFocusManager.getCurrentKeyboardFocusManager()
		    .addKeyEventDispatcher(this.keyEventDispatcher);
	}
    }

    /**
     * ########## GUI FLOW ########## - Resetting values - Resetting parameters
     */

    public void reset() throws ResourceException {
	resetSelections(btnAutomatic_Mode, btnManual_Mode);
	selectedLanguages = new ArrayList<>();
	setKeyEventDispatcher(false);
	repaint();
	revalidate();
    }

    private void setLanguageAndMode() {
	// if (btnManual_Mode.isSelected() && selectedLanguages.size() > 1) {
	// selectedLanguages.remove(0);
	// }
	root.setLanguageAndMode(selectedLanguages, btnManual_Mode.isSelected());
    }

    private void resetSelections(JButton thisButton, JButton otherButton) {
	// Erase all selections in language menu
	unselectAllItems();

	// Select new button
	thisButton.setSelected(true);
	thisButton.setFocusable(true);

	// Unselect previous ones
	otherButton.setSelected(false);
	otherButton.setFocusable(false);

	// Disable 'next' button
	btnNext_Mode.setEnabled(false);

	// Change text and maximum languages
	changeMaxLangs();
    }

    private boolean unlockNext() {
	// Checks whether it is possible to enable the 'Next' button
	if ((btnManual_Mode.isSelected() || btnAutomatic_Mode.isSelected())
		&& !selectedLanguages.isEmpty()) {
	    btnNext_Mode.setEnabled(true);
	    return true;
	} else {
	    btnNext_Mode.setEnabled(false);
	    return false;
	}
    }

    private void changeMaxLangs() {
	if (btnAutomatic_Mode.isSelected()) {
	    maxLangs = 3;
	} else {
	    maxLangs = Integer.MAX_VALUE;
	}
	changeMaxLangsText();
    }

    private void changeMaxLangsText() {
	// Define a regular expression pattern to match the last number in the
	// string
	if (maxLangs < Integer.MAX_VALUE) {
	    lblLanguage.setText(
		    root.getMessages().getString("label.mode.language"));
	} else {
	    lblLanguage.setText(
		    root.getMessages().getString("label.mode.language.manual"));
	}
    }

    public void unselectAllItems() {
	selectedLanguages = new ArrayList<>();
	if (!languagesMenu.getSelectedValuesList().isEmpty()) {
	    ListModel<JCheckBox> model = languagesMenu.getModel();
	    languagesMenu.setSelectedIndices(new int[languages.size()]);
	    for (int i = 0; i < model.getSize(); i++) {
		model.getElementAt(i).setSelected(false);
	    }
	}
    }

    /**
     * ########## GUI ELEMENTS ########## - Swing components - Languages menu +
     * Checkbox renderer
     */

    private JPanel getNorthPanel_Mode() throws ResourceException {
	if (northPanel_Mode == null) {
	    northPanel_Mode = new JPanel();
	    northPanel_Mode.setBounds(0, 0, 586, 111);
	    northPanel_Mode.setBackground(SystemColor.window);
	    northPanel_Mode.setLayout(null);
	    northPanel_Mode.add(getLblChooseTranslation());
	}
	return northPanel_Mode;
    }

    private JPanel getDownPanel_Mode() throws ResourceException {
	if (downPanel_Mode == null) {
	    downPanel_Mode = new JPanel();
	    downPanel_Mode.setBounds(0, 362, 586, 56);
	    downPanel_Mode.setLayout(new BorderLayout(0, 0));
	    downPanel_Mode.add(getBackPanel_Mode());
	}
	return downPanel_Mode;
    }

    private JPanel getCenterPanel_Mode() throws ResourceException {
	if (centerPanel_Mode == null) {
	    centerPanel_Mode = new JPanel();
	    centerPanel_Mode.setBounds(0, 109, 586, 255);
	    centerPanel_Mode.setBackground(SystemColor.window);
	    centerPanel_Mode.setLayout(null);
	    centerPanel_Mode.add(getScrollPane());
	    centerPanel_Mode.add(getLblLanguage());
	    centerPanel_Mode.add(getBtnManual_Mode());
	    centerPanel_Mode.add(getBtnAutomatic_Mode());
	    centerPanel_Mode.add(getLblChoose());
	}
	return centerPanel_Mode;
    }

    private JButton getBtnManual_Mode() throws ResourceException {
	if (btnManual_Mode == null) {
	    btnManual_Mode = new JButton(
		    root.getMessages().getString("button.mode.manual"));
	    btnManual_Mode.setMnemonic(btnManual_Mode.getText().charAt(0));
	    btnManual_Mode.setFocusable(false);
	    btnManual_Mode.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    resetSelections(btnManual_Mode, btnAutomatic_Mode);
		}
	    });
	    btnManual_Mode.setBounds(67, 44, 210, 52);
	    btnManual_Mode.setFont(ResourceLoader.getFont().deriveFont(20f));
	}
	return btnManual_Mode;
    }

    private JButton getBtnAutomatic_Mode() throws ResourceException {
	if (btnAutomatic_Mode == null) {
	    btnAutomatic_Mode = new JButton(
		    root.getMessages().getString("button.mode.auto"));
	    btnAutomatic_Mode
		    .setMnemonic(btnAutomatic_Mode.getText().charAt(0));
	    btnAutomatic_Mode.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    resetSelections(btnAutomatic_Mode, btnManual_Mode);
		    root.setMode(null);
		}
	    });
	    btnAutomatic_Mode.setFont(ResourceLoader.getFont().deriveFont(20f));
	    btnAutomatic_Mode.setBounds(305, 44, 210, 52);
	    btnAutomatic_Mode.setSelected(true);
	}
	return btnAutomatic_Mode;
    }

    private JList<JCheckBox> getLanguagesMenu() throws ResourceException {
	if (languagesMenu == null) {
	    DefaultListModel<JCheckBox> model = new DefaultListModel<JCheckBox>();
	    model.addAll(populateMenu());
	    languagesMenu = new JList<JCheckBox>(model);
	    languagesMenu.setBackground(SystemColor.window);
	    languagesMenu.setOpaque(false);
	    languagesMenu.setFont(ResourceLoader.getFont());
	    languagesMenu.setFont(ResourceLoader.getFont().deriveFont(15f));
	    languagesMenu.setSelectionMode(
		    ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	    languagesMenu.setCellRenderer(new CBRenderer());

	    languagesMenu.addListSelectionListener(new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
		    if (!e.getValueIsAdjusting()) {
			int selectedIndex = languagesMenu.getSelectedIndex();
			JCheckBox selectedCheckBox = null;

			if (selectedIndex == -1) {
			    selectedCheckBox = languagesMenu.getModel()
				    .getElementAt(0);
			} else if (selectedIndex > -1) {
			    selectedCheckBox = languagesMenu.getModel()
				    .getElementAt(selectedIndex);
			}

			boolean toSelect = !selectedCheckBox.isSelected();
			selectedCheckBox.setSelected(toSelect);

			// Check whether app can proceed to next card
			unlockNext();

		    }
		}
	    });

	}
	return languagesMenu;
    }

    class CBRenderer extends JCheckBox implements ListCellRenderer<JCheckBox> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Component getListCellRendererComponent(
		JList<? extends JCheckBox> list, JCheckBox value, int index,
		boolean isSelected, boolean cellHasFocus) {
	    // Set the state of the checkbox based on the selected state of the
	    // list cell
	    setSelected(value.isSelected());
	    setEnabled(value.isEnabled());
	    setText(value.getText());
	    setBackground(list.getBackground());
	    setForeground(list.getForeground());
	    setFont(list.getFont());
	    setOpaque(true);

	    if (countSelectedItems() >= maxLangs) {
		if (!value.isSelected()) {
		    setEnabled(false);
		    setVisible(false);
		}
	    } else {
		if (languagesMenu.isEnabled()) {
		    setEnabled(true);
		    setVisible(true);

		}
	    }

	    return this;
	}
    }

    private int countSelectedItems() {
	int count = 0;
	ListModel<JCheckBox> model = languagesMenu.getModel();
	for (int i = 0; i < model.getSize(); i++) {
	    if (model.getElementAt(i).isSelected()) {
		count++;
	    }
	}
	return count;
    }

    /**
     * Builds the JList model (alphabetically-sorted languages that make up a
     * multi-selectable list).
     * 
     * @return list of selectable check box menu items
     */
    private List<JCheckBox> populateMenu() {
	// Add all elements to combo box
	this.languages = ResourceLoader
		.getSupportedLanguages(root.getMessages());
	Collections.sort(languages);

	// Items
	List<JCheckBox> items = new ArrayList<>();
	for (int i = 0; i < languages.size(); i++) {
	    String l = languages.get(i);
	    JCheckBox newItem = new JCheckBox(l);
	    items.add(newItem);
	    newItem.addItemListener(new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
		    // Selection
		    if (e.getStateChange() == ItemEvent.SELECTED) {
			if (countSelectedItems() > maxLangs) {
			    ((JCheckBox) e.getItem()).setSelected(false);
			} else {
			    selectedLanguages
				    .add(((JCheckBox) e.getItem()).getText());
			}
			// Deselection
		    } else {
			selectedLanguages
				.remove(((JCheckBox) e.getItem()).getText());
		    }
		}

	    });
	}
	return items;
    }

    private JLabel getLblChoose() throws ResourceException {
	if (lblChoose == null) {
	    lblChoose = new JLabel(
		    root.getMessages().getString("label.mode.type"));
	    lblChoose.setLabelFor(getBtnAutomatic_Mode());
	    lblChoose.setForeground(Color.decode("#0089d6"));
	    lblChoose.setHorizontalAlignment(SwingConstants.CENTER);
	    lblChoose.setBounds(67, 11, 448, 22);
	    lblChoose.setFont(ResourceLoader.getFont().deriveFont(15f));
	}
	return lblChoose;
    }

    private JLabel getLblLanguage() throws ResourceException {
	if (lblLanguage == null) {
	    lblLanguage = new JLabel(
		    root.getMessages().getString("label.mode.language"));
	    lblLanguage.setLabelFor(getLanguagesMenu());
	    lblLanguage.setHorizontalAlignment(SwingConstants.CENTER);
	    lblLanguage.setForeground(Color.decode("#0089d6"));
	    lblLanguage.setFont(ResourceLoader.getFont().deriveFont(15f));
	    lblLanguage.setBounds(67, 107, 448, 19);
	}
	return lblLanguage;
    }

    private JLabel getLblChooseTranslation() throws ResourceException {
	if (lblChooseTranslation == null) {
	    lblChooseTranslation = new JLabel(
		    root.getMessages().getString("label.mode.title"));
	    lblChooseTranslation.setBounds(0, 0, 586, 111);
	    lblChooseTranslation.setHorizontalAlignment(SwingConstants.CENTER);
	    lblChooseTranslation
		    .setFont(ResourceLoader.getFont().deriveFont(40f));
	}
	return lblChooseTranslation;
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
		    reset();
		    root.show("file");
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
		    setLanguageAndMode();
		    setKeyEventDispatcher(false);
		    if (btnAutomatic_Mode.isSelected()) {
			root.show("automode");
		    } else {
			root.show("manual");
		    }
		}
	    });
	    btnNext_Mode.setBounds(421, 11, 115, 35);
	    btnNext_Mode.setMnemonic(btnNext_Mode.getText().charAt(0));
	    btnNext_Mode.setFont(ResourceLoader.getFont().deriveFont(14f));
	    btnNext_Mode.setEnabled(false);
	}
	return btnNext_Mode;
    }

    private JButton getBtnHelp_Mode() throws ResourceException {
	if (btnHelp_Mode == null) {
	    btnHelp_Mode = new JButton("");
	    btnHelp_Mode.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    JFrame hm = new HelpMode(root);
		    hm.setVisible(true);
		}
	    });
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

    private JScrollPane getScrollPane() throws ResourceException {
	if (scrollPane == null) {
	    scrollPane = new JScrollPane();
	    scrollPane.setBounds(154, 137, 280, 118);
	    scrollPane.setViewportView(getLanguagesMenu());
	}
	return scrollPane;
    }
}
