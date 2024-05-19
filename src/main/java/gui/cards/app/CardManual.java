package main.java.gui.cards.app;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.java.gui.cards.MainWindow;
import main.java.gui.cards.help.HelpManual;
import main.java.util.exception.ResourceException;
import main.java.util.properties.ResourceLoader;

public class CardManual extends JPanel {

    private static final long serialVersionUID = 1L;
    private MainWindow root;

    /*
     * Panels
     */
    private JPanel northPanel_Manual;
    private JPanel centerPanel_Manual;
    private JPanel downPanel_Manual;
    private JPanel backPanel_Manual;

    /*
     * Labels & text
     */
    private JButton btnTranslate_Manual;
    private JLabel lblBack_Manual;
    private JButton btnBack_Manual;

    private JFileChooser fileChooser;
    private JLabel lblLanguage;
    private JLabel lblFileLocation;
    private JButton btnNext_Manual;
    private JTextField txtPath;
    private JButton btnBrowse;
    private JTextArea txtLanguage;

    private String sourcePath;
    private JLabel lblManualTitle;
    private JButton btnHelp_Manual;
    private JLabel txtExplanation;

    /*
     * Mnemonics
     */
    private KeyEventDispatcher keyEventDispatcher;

    public CardManual(MainWindow root) throws ResourceException {
	this.root = root;

	this.setLayout(null);
	this.add(getNorthPanel_Manual());
	this.add(getCenterPanel_Manual());
	this.add(getDownPanel_Manual());
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
			    getBtnHelp_Manual().doClick();
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

    public void setSourcePath(String path) {
	this.sourcePath = path;
    }

    public void setLanguages(List<String> language) {
	List<String> langs = new ArrayList<>();
	int i = 0;
	for (String l : language) {
	    if (i < 5) {
		langs.add(l.split(", ")[0]);
	    }
	    i++;
	}
	if (i - 5 > 0) {
	    getTxtLanguage().setText(
		    String.join(", ", langs) + "... (+" + (i - 5) + ")");
	} else {
	    getTxtLanguage().setText(String.join(", ", langs));
	}
    }

    public void reset() {
	btnTranslate_Manual.setEnabled(false);
	btnNext_Manual.setEnabled(false);
	txtPath.setText("");
	getTxtLanguage().setText("");
	setKeyEventDispatcher(false);
    }

    private boolean getSaveFileChooser() {
	if (root.hasDirectory()) {
	    return true;
	}

	// By default
	fileChooser = new JFileChooser(this.sourcePath);
	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	int returnVal = fileChooser.showSaveDialog(this);

	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    txtPath.setText(fileChooser.getSelectedFile().getPath());
	    root.to(fileChooser.getSelectedFile().getAbsolutePath());
	    return true;
	}
	return false;
    }

    private JPanel getCenterPanel_Manual() throws ResourceException {
	if (centerPanel_Manual == null) {
	    centerPanel_Manual = new JPanel();
	    centerPanel_Manual.setBounds(0, 107, 586, 256);
	    centerPanel_Manual.setLayout(null);
	    centerPanel_Manual.setBackground(SystemColor.window);
	    centerPanel_Manual.add(getBtnTranslate_Manual());
	    centerPanel_Manual.add(getLblLanguage());
	    centerPanel_Manual.add(getLblFileLocation());
	    centerPanel_Manual.add(getTxtPath());
	    centerPanel_Manual.add(getBtnBrowse());
	    centerPanel_Manual.add(getTxtLanguage());
	    centerPanel_Manual.add(getTxtExplanation());
	}
	return centerPanel_Manual;
    }

    private JButton getBtnTranslate_Manual() throws ResourceException {
	if (btnTranslate_Manual == null) {
	    btnTranslate_Manual = new JButton(
		    root.getMessages().getString("button.translate"));
	    btnTranslate_Manual
		    .setMnemonic(btnTranslate_Manual.getText().charAt(0));
	    btnTranslate_Manual.setIcon(new ImageIcon(
		    CardManual.class.getResource("/img/save-icon.png")));
	    btnTranslate_Manual.setEnabled(false);
	    btnTranslate_Manual.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    root.setMode(
			    fileChooser.getSelectedFile().getAbsolutePath());
		    root.translate();
		    btnNext_Manual.setEnabled(true);
		}
	    });
	    btnTranslate_Manual
		    .setFont(ResourceLoader.getFont().deriveFont(15f));
	    btnTranslate_Manual.setFocusable(false);
	    btnTranslate_Manual.setBounds(222, 191, 153, 42);
	}
	return btnTranslate_Manual;
    }

    private JPanel getNorthPanel_Manual() throws ResourceException {
	if (northPanel_Manual == null) {
	    northPanel_Manual = new JPanel();
	    northPanel_Manual.setBackground(SystemColor.window);
	    northPanel_Manual.setBounds(0, 0, 586, 111);
	    northPanel_Manual.setLayout(null);
	    northPanel_Manual.add(getLblManualTitle());
	}
	return northPanel_Manual;
    }

    private JPanel getDownPanel_Manual() throws ResourceException {
	if (downPanel_Manual == null) {
	    downPanel_Manual = new JPanel();
	    downPanel_Manual.setBounds(0, 362, 586, 56);
	    downPanel_Manual.setLayout(new GridLayout(0, 1, 0, 0));
	    downPanel_Manual.add(getBackPanel_Manual());
	}
	return downPanel_Manual;
    }

    private JPanel getBackPanel_Manual() throws ResourceException {
	if (backPanel_Manual == null) {
	    backPanel_Manual = new JPanel();
	    backPanel_Manual.setLayout(null);
	    backPanel_Manual.setBackground(SystemColor.window);
	    backPanel_Manual.add(getLblBack_Manual());
	    backPanel_Manual.add(getBtnBack_Manual());
	    backPanel_Manual.add(getBtnNext_Manual());
	    backPanel_Manual.add(getBtnHelp_Manual());
	}
	return backPanel_Manual;
    }

    private JLabel getLblBack_Manual() {
	if (lblBack_Manual == null) {
	    lblBack_Manual = new JLabel("<");
	    lblBack_Manual.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    reset();
		    btnBack_Manual.doClick();
		}
	    });
	    lblBack_Manual.setFont(lblBack_Manual.getFont().deriveFont(15f));
	    lblBack_Manual.setBounds(10, 11, 31, 37);
	}
	return lblBack_Manual;
    }

    private JButton getBtnBack_Manual() {
	if (btnBack_Manual == null) {
	    btnBack_Manual = new JButton("");
	    btnBack_Manual.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    reset();
		    root.show("mode");
		}
	    });
	    btnBack_Manual.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/img/home-icon.png")));
	    btnBack_Manual.setMnemonic('b');
	    btnBack_Manual.setBorder(null);
	    btnBack_Manual.setBackground(SystemColor.window);
	    btnBack_Manual.setBounds(20, 11, 31, 37);
	}
	return btnBack_Manual;
    }

    private JLabel getLblLanguage() throws ResourceException {
	if (lblLanguage == null) {
	    lblLanguage = new JLabel(
		    root.getMessages().getString("label.manual.language"));
	    lblLanguage.setForeground(Color.decode("#0089d6"));
	    lblLanguage.setHorizontalAlignment(SwingConstants.CENTER);
	    lblLanguage.setBounds(10, 84, 214, 32);
	    lblLanguage.setFont(ResourceLoader.getFont().deriveFont(15f));
	}
	return lblLanguage;
    }

    private JLabel getLblFileLocation() throws ResourceException {
	if (lblFileLocation == null) {
	    lblFileLocation = new JLabel(
		    root.getMessages().getString("label.manual.file"));
	    lblFileLocation.setHorizontalAlignment(SwingConstants.CENTER);
	    lblFileLocation.setForeground(Color.decode("#0089d6"));
	    lblFileLocation.setFont(ResourceLoader.getFont().deriveFont(15f));
	    lblFileLocation.setBounds(10, 122, 214, 32);
	}
	return lblFileLocation;
    }

    private JButton getBtnNext_Manual() throws ResourceException {
	if (btnNext_Manual == null) {
	    btnNext_Manual = new JButton(
		    root.getMessages().getString("button.next"));
	    btnNext_Manual.setEnabled(false);
	    btnNext_Manual.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    setKeyEventDispatcher(false);
		    root.show("end");
		}
	    });
	    btnNext_Manual.setMnemonic(btnNext_Manual.getText().charAt(0));
	    btnNext_Manual.setFont(ResourceLoader.getFont().deriveFont(14f));
	    btnNext_Manual.setBounds(421, 11, 115, 35);
	}
	return btnNext_Manual;
    }

    private JTextField getTxtPath() throws ResourceException {
	if (txtPath == null) {
	    txtPath = new JTextField();
	    txtPath.setHorizontalAlignment(SwingConstants.CENTER);
	    txtPath.setFont(ResourceLoader.getFont().deriveFont(12f));
	    txtPath.setEditable(false);
	    txtPath.setColumns(10);
	    txtPath.setBounds(222, 125, 178, 30);
	}
	return txtPath;
    }

    private JButton getBtnBrowse() throws ResourceException {
	if (btnBrowse == null) {
	    btnBrowse = new JButton(
		    root.getMessages().getString("button.browse"));
	    btnBrowse.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    if (getSaveFileChooser()) {
			btnTranslate_Manual.setEnabled(true);
		    }
		}
	    });
	    btnBrowse.setMnemonic(btnBrowse.getText().charAt(0));
	    btnBrowse.setFont(ResourceLoader.getFont().deriveFont(14f));
	    btnBrowse.setBounds(410, 127, 115, 23);
	}
	return btnBrowse;
    }

    private JTextArea getTxtLanguage() throws ResourceException {
	if (txtLanguage == null) {
	    txtLanguage = new JTextArea();
	    txtLanguage.setBounds(222, 89, 303, 29);
	    txtLanguage.setBackground(SystemColor.window);
	    txtLanguage.setFont(ResourceLoader.getFont().deriveFont(14f));
	    txtLanguage.setEditable(false);
	    // 1 row
	    txtLanguage.setLineWrap(false);
	    txtLanguage.setWrapStyleWord(false);
	    txtLanguage.setPreferredSize(new java.awt.Dimension(500, 40));
	}
	return txtLanguage;
    }

    private JLabel getLblManualTitle() throws ResourceException {
	if (lblManualTitle == null) {
	    lblManualTitle = new JLabel(
		    root.getMessages().getString("label.manual.title"));
	    lblManualTitle.setHorizontalAlignment(SwingConstants.CENTER);
	    lblManualTitle.setFont(ResourceLoader.getFont().deriveFont(40f));
	    lblManualTitle.setBounds(0, 0, 586, 110);

	}
	return lblManualTitle;
    }

    private JButton getBtnHelp_Manual() {
	if (btnHelp_Manual == null) {
	    btnHelp_Manual = new JButton("");
	    btnHelp_Manual.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    JFrame hm = new HelpManual(root);
		    hm.setVisible(true);
		}
	    });
	    btnHelp_Manual.setToolTipText((String) null);
	    btnHelp_Manual.setToolTipText(
		    root.getMessages().getString("tooltip.manual"));
	    btnHelp_Manual.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/img/help.png")));
	    btnHelp_Manual.setFont(ResourceLoader.getFont().deriveFont(14f));
	    btnHelp_Manual.setFocusable(false);
	    btnHelp_Manual.setBorder(null);
	    btnHelp_Manual.setBackground(SystemColor.window);
	    btnHelp_Manual.setBounds(537, 7, 49, 41);
	}
	return btnHelp_Manual;
    }

    private JLabel getTxtExplanation() {
	if (txtExplanation == null) {
	    txtExplanation = new JLabel(
		    root.getMessages().getString("label.manual.explanation"));
	    txtExplanation.setHorizontalAlignment(SwingConstants.CENTER);
	    txtExplanation.setFont(ResourceLoader.getFont().deriveFont(15f));
	    txtExplanation.setBackground(Color.WHITE);
	    txtExplanation.setBounds(10, 11, 566, 62);
	    txtExplanation.setForeground(Color.decode("#0089d6"));
	}
	return txtExplanation;
    }
}
