package main.java.gui.cards;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import main.java.logic.util.exception.ResourceException;
import main.java.logic.util.properties.ResourceLoader;

public class CardAutoMode extends JPanel {

    private static final long serialVersionUID = 1L;
    private MainWindow root;

    /*
     * Panels
     */
    private JPanel northPanel_AutoMode;
    private JPanel downPanel_AutoMode;
    private JPanel centerPanel_Mode;
    private JLabel lblAutoSettings;
    private JLabel lblDefault;
    private JPanel backPanel_AutoMode;
    private JLabel lblBack_Mode;
    private JButton btnBack_Mode;
    private JButton btnNext_Mode;
    private JButton btnHelp_Mode;

    /*
     * Buttons
     */
    private ButtonGroup buttonGroup;
    private List<AbstractButton> buttons = new ArrayList<>();

    /*
     * Selected languages
     */
    private List<String> languages = new ArrayList<>();
    private String defaultLanguage = null;

    public CardAutoMode(MainWindow root) throws ResourceException {
	this.root = root;

	this.setBackground(SystemColor.window);
	this.setLayout(null);
	this.add(getDownPanel_AutoMode());
	this.add(getNorthPanel_AutoMode());
	getButtonGroup();
	this.add(getCenterPanel_Mode());
    }

    /**
     * ########## GUI FLOW ########## - Resetting values - Resetting parameters
     */

    public void reset() throws ResourceException {
	for (AbstractButton b : buttons) {
	    buttonGroup.remove(b);
	}
    }

    /**
     * Sets the languages that the user has chosen for automatic translation
     * purposes, and creates the respective radio buttons in this card.
     * 
     * @param languages list of language-country strings
     * @throws ResourceException
     */
    public void setTargetLanguages(List<String> languages)
	    throws ResourceException {
	this.languages = languages;
	addButtons();
    }

    /**
     * ########## GUI ELEMENTS ##########
     */

    private JPanel getNorthPanel_AutoMode() throws ResourceException {
	if (northPanel_AutoMode == null) {
	    northPanel_AutoMode = new JPanel();
	    northPanel_AutoMode.setBounds(0, 0, 586, 111);
	    northPanel_AutoMode.setBackground(SystemColor.window);
	    northPanel_AutoMode.setLayout(null);
	    northPanel_AutoMode.add(getLblAutoSettings());
	}
	return northPanel_AutoMode;
    }

    private JPanel getDownPanel_AutoMode() throws ResourceException {
	if (downPanel_AutoMode == null) {
	    downPanel_AutoMode = new JPanel();
	    downPanel_AutoMode.setBounds(0, 362, 586, 56);
	    downPanel_AutoMode.setLayout(new BorderLayout(0, 0));
	    downPanel_AutoMode.add(getBackPanel_AutoMode());
	}
	return downPanel_AutoMode;
    }

    private JPanel getCenterPanel_Mode() throws ResourceException {
	if (centerPanel_Mode == null) {
	    centerPanel_Mode = new JPanel();
	    centerPanel_Mode.setBounds(0, 110, 586, 254);
	    centerPanel_Mode.setBackground(SystemColor.window);
	    centerPanel_Mode.setLayout(null);
	    centerPanel_Mode.add(getLblDefault());
	    getButtonGroup();
	}
	return centerPanel_Mode;
    }

    private List<AbstractButton> addButtons() throws ResourceException {
	// For aesthetics
	int y = 80;
	int SEP = 40;

	for (String language : languages) {
	    JRadioButton radioButton = new JRadioButton();
	    radioButton.setText(language);
	    radioButton.setHorizontalAlignment(SwingConstants.LEFT);
	    radioButton.setBackground(SystemColor.window);
	    radioButton.setBounds(200, y, 250, 30);
	    radioButton.setFont(ResourceLoader.getFont().deriveFont(14f));

	    radioButton.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
		    if (radioButton.isSelected()) {
			defaultLanguage = radioButton.getText();
		    } else {
			defaultLanguage = null;
		    }
		}

	    });

	    y += SEP;
	    centerPanel_Mode.add(radioButton);
	    buttonGroup.add(radioButton);
	    buttons.add(radioButton);
	}

	return buttons;
    }

    private JLabel getLblDefault() throws ResourceException {
	if (lblDefault == null) {
	    lblDefault = new JLabel(
		    root.getMessages().getString("label.settings.default"));
	    lblDefault.setForeground(Color.decode("#0089d6"));
	    lblDefault.setHorizontalAlignment(SwingConstants.CENTER);
	    lblDefault.setBounds(10, 31, 566, 22);
	    lblDefault.setFont(ResourceLoader.getFont().deriveFont(18f));
	}
	return lblDefault;
    }

    private ButtonGroup getButtonGroup() throws ResourceException {
	if (buttonGroup == null) {
	    buttonGroup = new ButtonGroup();
	}
	return buttonGroup;
    }

    private JLabel getLblAutoSettings() throws ResourceException {
	if (lblAutoSettings == null) {
	    lblAutoSettings = new JLabel(
		    root.getMessages().getString("label.settings.title"));
	    lblAutoSettings.setBounds(0, 0, 586, 111);
	    lblAutoSettings.setHorizontalAlignment(SwingConstants.CENTER);
	    lblAutoSettings.setFont(ResourceLoader.getFont().deriveFont(40f));
	}
	return lblAutoSettings;
    }

    private JPanel getBackPanel_AutoMode() throws ResourceException {
	if (backPanel_AutoMode == null) {
	    backPanel_AutoMode = new JPanel();
	    backPanel_AutoMode.setBackground(SystemColor.window);
	    backPanel_AutoMode.setLayout(null);
	    backPanel_AutoMode.add(getLblBack_Mode());
	    backPanel_AutoMode.add(getBtnBack_Mode());
	    backPanel_AutoMode.add(getBtnNext_Mode());
	    backPanel_AutoMode.add(getBtnHelp_Mode());
	}
	return backPanel_AutoMode;
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
			reset();
			root.show("mode");
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
		    root.setLanguages(languages, defaultLanguage);

		    try {
			root.show("image");
		    } catch (Exception e1) {
			root.showErrorMessage(e1.getMessage());
		    }

		}

	    });
	    btnNext_Mode.setBounds(421, 11, 115, 35);
	    btnNext_Mode.setMnemonic('n');
	    btnNext_Mode.setFont(ResourceLoader.getFont().deriveFont(14f));
	    btnNext_Mode.setEnabled(true);
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
}
