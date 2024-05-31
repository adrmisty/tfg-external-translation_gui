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
import main.java.util.exception.ResourceException;
import main.java.util.resources.ResourceLoader;

public class HelpTTS extends JFrame {

    private static final long serialVersionUID = 1L;
    private MainWindow root;
    private JLabel lblHelpDefault;
    private JTextPane txtDefault;
    private JLabel lblImage;

    public HelpTTS(MainWindow root) throws ResourceException {
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

    }

    private JLabel getLblFileLingual_1() {
	if (lblHelpDefault == null) {
	    lblHelpDefault = new JLabel(
		    root.getMessages().getString("help.read.title"));
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
	    txtDefault.setText(root.getMessages().getString("help.read.text1"));
	    txtDefault.setFont(ResourceLoader.getFont().deriveFont(15f));
	    txtDefault.setEditable(false);
	    txtDefault.setForeground(SystemColor.textInactiveText);
	    txtDefault.setBackground(SystemColor.window);
	    txtDefault.setBounds(37, 271, 505, 131);
	}
	return txtDefault;
    }

    private JLabel getLblImage() {
	if (lblImage == null) {
	    lblImage = new JLabel("");
	    lblImage.setIcon(new ImageIcon(
		    HelpTTS.class.getResource("/helpimg/help-read.png")));
	    lblImage.setBounds(146, 72, 322, 205);
	}
	return lblImage;
    }
}
