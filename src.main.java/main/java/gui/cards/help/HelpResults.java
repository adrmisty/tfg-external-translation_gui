package main.java.gui.cards.help;

import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import main.java.gui.cards.MainWindow;
import main.java.util.ResourceLoader;
import main.java.util.exception.ResourceException;

public class HelpResults extends JFrame {

    private static final long serialVersionUID = 1L;
    private MainWindow root;
    private JLabel lblHelpDefault;
    private JTextPane txtDefault;
    private JLabel lblImage;
    private JLabel lblImage2;
    private JTextPane txtDefault2;

    public HelpResults(MainWindow root) throws ResourceException {
	getContentPane().setBackground(SystemColor.textHighlightText);
	this.root = root;

	setIconImage(Toolkit.getDefaultToolkit()
		.getImage(MainWindow.class.getResource("/img/icon.png")));

	setBackground(SystemColor.window);
	setResizable(false);
	setAutoRequestFocus(false);
	setTitle("FileLingual");
	setBounds(0, 0, 600, 450);
	setLocationRelativeTo(null);
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	getContentPane().setLayout(null);
	getContentPane().add(getLblFileLingual_1());
	getContentPane().add(getTxtDefault());
	getContentPane().add(getLblImage());
	getContentPane().add(getLblImage2());
	getContentPane().add(getTxtDefault2());

    }

    private JLabel getLblFileLingual_1() {
	if (lblHelpDefault == null) {
	    lblHelpDefault = new JLabel(
		    root.getMessages().getString("help.results.title"));
	    lblHelpDefault.setHorizontalAlignment(SwingConstants.CENTER);
	    lblHelpDefault.setForeground(Color.black);
	    lblHelpDefault.setFont(ResourceLoader.getFont().deriveFont(40f));
	    lblHelpDefault.setBounds(10, 11, 566, 61);
	}
	return lblHelpDefault;
    }

    private JTextPane getTxtDefault() {
	if (txtDefault == null) {
	    txtDefault = new JTextPane();
	    txtDefault.setText(
		    root.getMessages().getString("help.results.text1"));
	    txtDefault.setFont(ResourceLoader.getFont().deriveFont(15f));
	    txtDefault.setEditable(false);
	    txtDefault.setForeground(SystemColor.textInactiveText);
	    txtDefault.setBackground(SystemColor.window);
	    txtDefault.setBounds(37, 234, 214, 168);
	}
	return txtDefault;
    }

    private JLabel getLblImage() {
	if (lblImage == null) {
	    lblImage = new JLabel("");
	    lblImage.setIcon(new ImageIcon(HelpResults.class
		    .getResource("/helpimg/help-results.png")));
	    lblImage.setBounds(146, 72, 322, 151);
	}
	return lblImage;
    }

    private JLabel getLblImage2() {
	if (lblImage2 == null) {
	    lblImage2 = new JLabel("");
	    lblImage2.setIcon(new ImageIcon(
		    HelpResults.class.getResource("/img/save-icon.png")));

	    lblImage2.setBounds(282, 282, 38, 73);
	}
	return lblImage2;
    }

    private JTextPane getTxtDefault2() {
	if (txtDefault2 == null) {
	    txtDefault2 = new JTextPane();
	    txtDefault2.setText(
		    root.getMessages().getString("help.results.text2"));
	    txtDefault2.setForeground(SystemColor.textInactiveText);
	    txtDefault2.setFont(ResourceLoader.getFont().deriveFont(15f));
	    txtDefault2.setEditable(false);
	    txtDefault2.setBackground(SystemColor.window);
	    txtDefault2.setBounds(362, 234, 214, 168);
	}
	return txtDefault2;
    }
}
