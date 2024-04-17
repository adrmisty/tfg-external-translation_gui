package main.java.gui.cards;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import main.java.logic.util.exception.ResourceException;
import main.java.logic.util.properties.ResourceLoader;

public class CardMain extends JPanel {

    private static final long serialVersionUID = 1L;
    private MainWindow root;

    /*
     * Panels
     */
    private JPanel northPanel;
    private JPanel downPanel;
    private JPanel centerPanel;
    private JPanel logoPanel;

    /*
     * Buttons
     */
    private JSplitPane splitPane;
    private JButton leftButton;
    private JButton rightButton;

    /*
     * Labels
     */
    private JLabel lblSlogan;
    private JLabel lblForLogo;
    private JLabel lblLogo;
    private JLabel lblFileLingual;

    public CardMain(MainWindow root) throws ResourceException {
	this.root = root;

	this.setLayout(null);
	this.add(getNorthPanel());
	this.add(getDownPanel());
	this.add(getCenterPanel());
    }

    private JPanel getNorthPanel() throws ResourceException {
	if (northPanel == null) {
	    northPanel = new JPanel();
	    northPanel.setBounds(0, 0, 586, 110);
	    northPanel.setBackground(SystemColor.window);
	    northPanel.setLayout(new BorderLayout(0, 0));
	    northPanel.add(getLblFileLingual(), BorderLayout.SOUTH);
	}
	return northPanel;
    }

    private JPanel getDownPanel() throws ResourceException {
	if (downPanel == null) {
	    downPanel = new JPanel();
	    downPanel.setBounds(0, 246, 586, 167);
	    downPanel.setBackground(SystemColor.window);
	    downPanel.setLayout(null);
	    downPanel.add(getSplitPane());
	}
	return downPanel;
    }

    private JButton getLeftButton() throws ResourceException {
	if (leftButton == null) {
	    leftButton = new JButton(
		    root.getMessages().getString("button.start"));
	    leftButton
		    .setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    leftButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    try {
			root.show("file");
		    } catch (Exception e1) {
			root.showErrorMessage(e1.getMessage());
		    }
		}
	    });
	    leftButton.setMnemonic('s');
	    leftButton.setFont(ResourceLoader.getFont().deriveFont(20f));
	}
	return leftButton;
    }

    @SuppressWarnings("unchecked")
    private JButton getRightButton() throws ResourceException {
	if (rightButton == null) {
	    // Underlined text
	    rightButton = new JButton(
		    root.getMessages().getString("button.learn"));
	    rightButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    // Open information window
		    try {
			root.show("info");
		    } catch (Exception e1) {
			root.showErrorMessage(e1.getMessage());
		    }
		}
	    });
	    rightButton.setFont(ResourceLoader.getFont().deriveFont(20f));
	    rightButton.setMnemonic('l');
	    rightButton
		    .setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    Font font = rightButton.getFont();
	    Map<TextAttribute, Integer> attributes = (Map<TextAttribute, Integer>) font
		    .getAttributes();
	    attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
	    rightButton.setFont(font.deriveFont(attributes));
	}
	return rightButton;
    }

    private JSplitPane getSplitPane() throws ResourceException {
	if (splitPane == null) {
	    splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		    getLeftButton(), getRightButton());
	    splitPane.setBounds(147, 30, 280, 35);
	}
	return splitPane;
    }

    private JPanel getCenterPanel() throws ResourceException {
	if (centerPanel == null) {
	    centerPanel = new JPanel();
	    centerPanel.setBackground(SystemColor.window);
	    centerPanel.setBounds(0, 110, 586, 136);
	    centerPanel.setLayout(new BorderLayout(0, 0));
	    centerPanel.add(getLblSlogan(), BorderLayout.NORTH);
	    centerPanel.add(getLogoPanel());
	}
	return centerPanel;
    }

    private JLabel getLblSlogan() throws ResourceException {
	if (lblSlogan == null) {
	    lblSlogan = new JLabel(
		    root.getMessages().getString("label.slogan"));
	    lblSlogan.setHorizontalAlignment(SwingConstants.CENTER);
	    lblSlogan.setFont(ResourceLoader.getFont().deriveFont(25f));
	    lblSlogan.setBackground(SystemColor.window);
	}
	return lblSlogan;
    }

    private JPanel getLogoPanel() throws ResourceException {
	if (logoPanel == null) {
	    logoPanel = new JPanel();
	    logoPanel.setBackground(SystemColor.window);
	    logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	    logoPanel.add(getLblForLogo());
	    logoPanel.add(getLblLogo());
	}
	return logoPanel;
    }

    private JLabel getLblForLogo() throws ResourceException {
	if (lblForLogo == null) {
	    lblForLogo = new JLabel(
		    root.getMessages().getString("label.poweredby"));
	    lblForLogo.setFont(ResourceLoader.getFont().deriveFont(20f));
	}
	return lblForLogo;
    }

    private JLabel getLblLogo() {
	if (lblLogo == null) {
	    lblLogo = new JLabel("");
	    lblLogo.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/img/openai-logo.png")));
	    lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
	}
	return lblLogo;
    }

    private JLabel getLblFileLingual() throws ResourceException {
	if (lblFileLingual == null) {
	    lblFileLingual = new JLabel("FileLingual");
	    lblFileLingual.setHorizontalAlignment(SwingConstants.CENTER);
	    lblFileLingual.setFont(ResourceLoader.getFont().deriveFont(50f));
	}
	return lblFileLingual;
    }

}
