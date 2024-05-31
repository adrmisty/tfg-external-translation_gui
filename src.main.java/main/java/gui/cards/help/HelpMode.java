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

public class HelpMode extends JFrame {

    private static final long serialVersionUID = 1L;
    private MainWindow root;
    private JLabel lblHelpMode;
    private JTextPane txtHelpMode;
    private JLabel lblImage;

    public HelpMode(MainWindow root) throws ResourceException {
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
	getContentPane().add(getTxtHelpMode());
	getContentPane().add(getLblImage());

    }

    private JLabel getLblFileLingual_1() {
	if (lblHelpMode == null) {
	    lblHelpMode = new JLabel(
		    root.getMessages().getString("help.mode.title"));
	    lblHelpMode.setHorizontalAlignment(SwingConstants.CENTER);
	    lblHelpMode.setForeground(Color.black);
	    lblHelpMode.setFont(ResourceLoader.getFont().deriveFont(40f));
	    lblHelpMode.setBounds(10, 11, 566, 61);
	}
	return lblHelpMode;
    }

    private JTextPane getTxtHelpMode() {
	if (txtHelpMode == null) {
	    txtHelpMode = new JTextPane();
	    txtHelpMode.setText(root.getMessages().getString("help.mode.text"));
	    txtHelpMode.setFont(ResourceLoader.getFont().deriveFont(15f));
	    txtHelpMode.setEditable(false);
	    txtHelpMode.setForeground(SystemColor.textInactiveText);
	    txtHelpMode.setBackground(SystemColor.window);
	    txtHelpMode.setBounds(28, 77, 535, 126);
	}
	return txtHelpMode;
    }

    private JLabel getLblImage() {
	if (lblImage == null) {
	    lblImage = new JLabel("");
	    lblImage.setIcon(new ImageIcon(
		    HelpMode.class.getResource("/helpimg/help-mode1.png")));
	    lblImage.setBounds(90, 203, 457, 199);
	}
	return lblImage;
    }
}
