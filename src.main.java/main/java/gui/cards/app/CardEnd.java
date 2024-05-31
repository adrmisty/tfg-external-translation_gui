package main.java.gui.cards.app;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import main.java.gui.cards.MainWindow;
import main.java.util.exception.ResourceException;
import main.java.util.resources.ResourceLoader;

public class CardEnd extends JPanel {

    private static final long serialVersionUID = 1L;
    private MainWindow root;

    /*
     * Panels
     */
    private JPanel northPanel_End;
    private JPanel downPanel_End;
    private JPanel centerPanel_End;

    /*
     * Labels & text
     */
    private JSplitPane splitPane_End;
    private JButton leftButton_End;
    private JButton rightButton_End;
    private JLabel lblThanks;
    private JLabel lblSlogan_End;
    private JTextPane lblFileSave;
    private JPanel logoPanel;
    private JLabel lblForLogo;
    private JLabel lblLogo;
    private JLabel lblLogo2;

    public CardEnd(MainWindow root) throws ResourceException {
	this.root = root;

	this.setBackground(SystemColor.window);
	this.setLayout(null);
	this.add(getNorthPanel_End());
	this.add(getDownPanel_End());
	this.add(getCenterPanel_End());

    }

    private JPanel getNorthPanel_End() {
	if (northPanel_End == null) {
	    northPanel_End = new JPanel();
	    northPanel_End.setBackground(SystemColor.window);
	    northPanel_End.setBounds(0, 0, 586, 119);
	    northPanel_End.setLayout(null);
	    northPanel_End.add(getLblThanks());
	    northPanel_End.add(getLblSlogan_End());
	}
	return northPanel_End;
    }

    private JPanel getDownPanel_End() throws ResourceException {
	if (downPanel_End == null) {
	    downPanel_End = new JPanel();
	    downPanel_End.setLayout(null);
	    downPanel_End.setBackground(SystemColor.window);
	    downPanel_End.setBounds(0, 246, 586, 167);
	    downPanel_End.add(getSplitPane_End());
	    downPanel_End.add(getLblFileSave());
	}
	return downPanel_End;
    }

    private JSplitPane getSplitPane_End() throws ResourceException {
	if (splitPane_End == null) {
	    splitPane_End = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		    (Component) null, (Component) null);
	    splitPane_End.setContinuousLayout(true);
	    splitPane_End.setBounds(113, 36, 371, 75);
	    splitPane_End.setLeftComponent(getLeftButton_End());
	    splitPane_End.setRightComponent(getRightButton_End());
	}
	return splitPane_End;
    }

    private JButton getLeftButton_End() throws ResourceException {
	if (leftButton_End == null) {
	    leftButton_End = new JButton(
		    root.getMessages().getString("label.more"));
	    leftButton_End.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    root.reset();
		}
	    });
	    leftButton_End.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/img/translate.png")));
	    leftButton_End.setMnemonic('t');
	    leftButton_End.setFont(ResourceLoader.getFont().deriveFont(15f));
	}
	return leftButton_End;
    }

    private JButton getRightButton_End() throws ResourceException {
	if (rightButton_End == null) {
	    rightButton_End = new JButton(
		    root.getMessages().getString("label.exit"));
	    rightButton_End.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    System.exit(0);
		}
	    });
	    rightButton_End.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/img/exit.png")));
	    rightButton_End.setMnemonic('e');
	    rightButton_End.setFont(ResourceLoader.getFont().deriveFont(15f));
	}
	return rightButton_End;
    }

    private JPanel getCenterPanel_End() {
	if (centerPanel_End == null) {
	    centerPanel_End = new JPanel();
	    centerPanel_End.setBackground(SystemColor.window);
	    centerPanel_End.setBounds(0, 119, 586, 127);
	    centerPanel_End.setLayout(null);
	    centerPanel_End.add(getLogoPanel());
	}
	return centerPanel_End;
    }

    private JLabel getLblThanks() {
	if (lblThanks == null) {
	    lblThanks = new JLabel(
		    root.getMessages().getString("label.thanks"));
	    lblThanks.setBounds(0, 11, 586, 37);
	    lblThanks.setHorizontalAlignment(SwingConstants.CENTER);
	    lblThanks.setFont(lblThanks.getFont().deriveFont(30f));
	}
	return lblThanks;
    }

    private JLabel getLblSlogan_End() {
	if (lblSlogan_End == null) {
	    lblSlogan_End = new JLabel("FileLingual");
	    lblSlogan_End.setBounds(0, 50, 586, 72);
	    lblSlogan_End.setHorizontalAlignment(SwingConstants.CENTER);
	    lblSlogan_End.setFont(lblSlogan_End.getFont().deriveFont(50f));
	    lblSlogan_End.setBackground(SystemColor.window);
	}
	return lblSlogan_End;
    }

    private JTextPane getLblFileSave() throws ResourceException {
	if (lblFileSave == null) {
	    lblFileSave = new JTextPane();
	    lblFileSave.setBackground(SystemColor.window);
	    lblFileSave.setEditable(false);
	    lblFileSave.setEditable(false);
	    lblFileSave.setFont(ResourceLoader.getFont().deriveFont(17f));
	    lblFileSave.setForeground(Color.BLUE.darker());
	    lblFileSave.setBounds(10, 0, 566, 25);
	}
	return lblFileSave;
    }

    public void setSavedFileName(String name, boolean saved) {

	if (saved) {
	    // Center alignment
	    StyledDocument doc = lblFileSave.getStyledDocument();
	    SimpleAttributeSet center = new SimpleAttributeSet();
	    StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
	    doc.setParagraphAttributes(0, doc.getLength(), center, false);

	    String text = root.getMessages().getString("label.translated") + " "
		    + name;
	    try {
		// Insert the text, centered
		doc.insertString(0, text, center);
	    } catch (BadLocationException e) {
		// If any error arises, just put it normally
		lblFileSave.setText(text);
	    }
	    lblFileSave.setFont(ResourceLoader.getFont().deriveFont(15f));
	}

    }

    private JPanel getLogoPanel() {
	if (logoPanel == null) {
	    logoPanel = new JPanel();
	    logoPanel.setBounds(0, 0, 586, 127);
	    logoPanel.setLayout(null);
	    logoPanel.setBackground(SystemColor.window);
	    logoPanel.add(getLblForLogo());
	    logoPanel.add(getLblLogo());
	    logoPanel.add(getLblLogo2());
	}
	return logoPanel;
    }

    private JLabel getLblForLogo() {
	if (lblForLogo == null) {
	    lblForLogo = new JLabel(
		    root.getMessages().getString("label.poweredby"));
	    lblForLogo.setHorizontalAlignment(SwingConstants.RIGHT);
	    lblForLogo.setForeground(Color.BLACK);
	    lblForLogo.setFont(lblForLogo.getFont().deriveFont(20f));
	    lblForLogo.setBounds(0, 0, 250, 127);
	}
	return lblForLogo;
    }

    private JLabel getLblLogo() {
	if (lblLogo == null) {
	    lblLogo = new JLabel("");
	    lblLogo.setIcon(new ImageIcon(
		    CardEnd.class.getResource("/img/openai-logo.png")));
	    lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
	    lblLogo.setBounds(285, 71, 132, 45);
	}
	return lblLogo;
    }

    private JLabel getLblLogo2() {
	if (lblLogo2 == null) {
	    lblLogo2 = new JLabel("");
	    lblLogo2.setIcon(
		    new ImageIcon(CardEnd.class.getResource("/img/azure.png")));
	    lblLogo2.setHorizontalAlignment(SwingConstants.CENTER);
	    lblLogo2.setBounds(275, 11, 144, 61);
	}
	return lblLogo2;
    }
}
