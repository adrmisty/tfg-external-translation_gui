package main.java.gui.cards;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
    private static int maxLangs = 3;
    private JScrollPane scrollPane;

    public CardMode(MainWindow root) throws ResourceException {
	this.root = root;

	this.setBackground(SystemColor.window);
	this.setLayout(null);
	this.add(getDownPanel_Mode());
	this.add(getNorthPanel_Mode());
	this.add(getCenterPanel_Mode());
    }

    public void reset() throws ResourceException {
	unselectAllItems();
	selectedLanguages = new ArrayList<>();
	unlockNext();
	btnManual_Mode.setSelected(false);
	btnAutomatic_Mode.doClick();
	repaint();
	revalidate();
    }

    public void unselectAllItems() {
	ListModel<JCheckBox> model = languagesMenu.getModel();
	languagesMenu.setSelectedIndices(new int[languages.size()]);
	for (int i = 0; i < model.getSize(); i++) {
	    model.getElementAt(i).setSelected(false);
	}
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
	    downPanel_Mode.setBounds(0, 364, 586, 49);
	    downPanel_Mode.setLayout(new BorderLayout(0, 0));
	    downPanel_Mode.add(getBackPanel_Mode());
	}
	return downPanel_Mode;
    }

    private JPanel getCenterPanel_Mode() throws ResourceException {
	if (centerPanel_Mode == null) {
	    centerPanel_Mode = new JPanel();
	    centerPanel_Mode.setBounds(0, 80, 586, 284);
	    centerPanel_Mode.setBackground(SystemColor.window);
	    centerPanel_Mode.setLayout(null);
	    centerPanel_Mode.add(getScrollPane());
	    centerPanel_Mode.add(getBtnManual_Mode());
	    centerPanel_Mode.add(getBtnAutomatic_Mode());
	    centerPanel_Mode.add(getLblChoose());
	    centerPanel_Mode.add(getLblLanguage());
	}
	return centerPanel_Mode;
    }

    private void setSelectedButton(JButton thisButton, JButton otherButton) {
	unselectAllItems();
	languagesMenu.setEnabled(true);
	languagesMenu.setVisible(true);
	languagesMenu.setOpaque(true);
	thisButton.setSelected(true);
	thisButton.setFocusable(true);
	otherButton.setSelected(false);
	otherButton.setFocusable(false);
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
	    btnManual_Mode.setBounds(67, 44, 210, 52);
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
	    btnAutomatic_Mode.setBounds(305, 44, 210, 52);
	    btnAutomatic_Mode.doClick();
	}
	return btnAutomatic_Mode;
    }

    private JList<JCheckBox> getLanguagesMenu() throws ResourceException {
	if (languagesMenu == null) {
	    DefaultListModel<JCheckBox> model = new DefaultListModel<JCheckBox>();
	    model.addAll(populateMenu());
	    languagesMenu = new JList<JCheckBox>(model);
	    languagesMenu.setEnabled(false);
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
			if (!selectedLanguages.contains(
				selectedLanguages.get(selectedIndex))) {
			    selectedLanguages.add(selectedCheckBox.getText());
			} else {
			    selectedLanguages
				    .remove(selectedCheckBox.getText());
			}

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
		    if (e.getStateChange() == ItemEvent.SELECTED) {
			// Check if the maximum limit is reached
			if (countSelectedItems() > maxLangs) {
			    ((JCheckBox) e.getItem()).setSelected(false);
			} else {
			    selectedLanguages
				    .add(((JCheckBox) e.getItem()).getText());
			}
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
	    lblChoose.setForeground(SystemColor.textHighlight);
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
	    lblLanguage.setForeground(SystemColor.textHighlight);
	    lblLanguage.setFont(ResourceLoader.getFont().deriveFont(15f));
	    lblLanguage.setBounds(67, 119, 448, 19);
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
		    for (String s : selectedLanguages) {
			System.out.println(s);
		    }
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

    private JScrollPane getScrollPane() throws ResourceException {
	if (scrollPane == null) {
	    scrollPane = new JScrollPane();
	    scrollPane.setBounds(155, 149, 280, 124);
	    scrollPane.setViewportView(getLanguagesMenu());
	}
	return scrollPane;
    }
}
