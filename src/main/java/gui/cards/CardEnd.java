package main.java.gui.cards;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import main.java.logic.util.exception.ResourceException;
import main.java.logic.util.properties.ResourceLoader;

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
    private JPanel logoPanel_End;
    private JLabel lblForLogo_End;
    private JLabel lblLogo_End;
    private JLabel lblFileSave;

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
		    try {
			root.reset();
			root.show("file");
		    } catch (Exception e1) {
			root.showErrorMessage(e1.getMessage());
		    }
		}
	    });
	    leftButton_End.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/img/translate.png")));
	    leftButton_End.setMnemonic('s');
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
	    rightButton_End.setMnemonic('l');
	    rightButton_End.setFont(ResourceLoader.getFont().deriveFont(15f));
	}
	return rightButton_End;
    }

    private JPanel getCenterPanel_End() {
	if (centerPanel_End == null) {
	    centerPanel_End = new JPanel();
	    centerPanel_End.setBackground(SystemColor.window);
	    centerPanel_End.setBounds(0, 119, 586, 127);
	    centerPanel_End.setLayout(new BorderLayout(0, 0));
	    centerPanel_End.add(getLogoPanel_End(), BorderLayout.SOUTH);
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

    private JPanel getLogoPanel_End() {
	if (logoPanel_End == null) {
	    logoPanel_End = new JPanel();
	    logoPanel_End.setBackground(SystemColor.window);
	    logoPanel_End.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	    logoPanel_End.add(getLblForLogo_End());
	    logoPanel_End.add(getLblLogo_End());
	}
	return logoPanel_End;
    }

    private JLabel getLblForLogo_End() {
	if (lblForLogo_End == null) {
	    lblForLogo_End = new JLabel(
		    root.getMessages().getString("label.poweredby"));
	    lblForLogo_End.setFont(lblForLogo_End.getFont().deriveFont(20f));
	}
	return lblForLogo_End;
    }

    private JLabel getLblLogo_End() {
	if (lblLogo_End == null) {
	    lblLogo_End = new JLabel("");
	    lblLogo_End.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/img/openai-logo.png")));
	    lblLogo_End.setHorizontalAlignment(SwingConstants.CENTER);
	}
	return lblLogo_End;
    }

    private JLabel getLblFileSave() throws ResourceException {
	if (lblFileSave == null) {
	    lblFileSave = new JLabel();
	    lblFileSave.setHorizontalAlignment(SwingConstants.CENTER);
	    lblFileSave.setFont(ResourceLoader.getFont().deriveFont(17f));
	    lblFileSave.setForeground(Color.BLUE.darker());
	    lblFileSave.setBounds(10, 0, 576, 25);
	}
	return lblFileSave;
    }

    public void setSavedFileName(String name) {
	lblFileSave.setText(
		root.getMessages().getString("label.translated") + " " + name);
    }

}
