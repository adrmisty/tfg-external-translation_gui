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

public class HelpFile extends JFrame {

    private static final long serialVersionUID = 1L;
    private MainWindow root;
    private JLabel lblHelpFile;
    private JLabel lblImage;
    private JTextPane txtFile;
    private JTextPane txtFile2;
    private JLabel lblImage2;
    private JTextPane txtFile3;

    public HelpFile(MainWindow root) throws ResourceException {
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
	getContentPane().add(getLblImage());
	getContentPane().add(getTxtFile());
	getContentPane().add(getTxtFile2());
	getContentPane().add(getLblImage2());
	getContentPane().add(getTxtFile3());

    }

    private JLabel getLblFileLingual_1() {
	if (lblHelpFile == null) {
	    lblHelpFile = new JLabel(
		    root.getMessages().getString("help.file.title"));
	    lblHelpFile.setHorizontalAlignment(SwingConstants.CENTER);
	    lblHelpFile.setForeground(Color.black);
	    lblHelpFile.setFont(ResourceLoader.getFont().deriveFont(40f));
	    lblHelpFile.setBounds(10, 11, 566, 61);
	}
	return lblHelpFile;
    }

    private JLabel getLblImage() {
	if (lblImage == null) {
	    lblImage = new JLabel("");
	    lblImage.setIcon(new ImageIcon(
		    HelpFile.class.getResource("/helpimg/help-file1.png")));
	    lblImage.setBounds(243, 206, 307, 67);
	}
	return lblImage;
    }

    private JTextPane getTxtFile() {
	if (txtFile == null) {
	    txtFile = new JTextPane();
	    txtFile.setText(root.getMessages().getString("help.file.text"));
	    txtFile.setFont(ResourceLoader.getFont().deriveFont(15f));
	    txtFile.setEditable(false);
	    txtFile.setForeground(Color.decode("#0089d6"));
	    txtFile.setBackground(SystemColor.window);
	    txtFile.setBounds(43, 93, 190, 99);
	}
	return txtFile;
    }

    private JTextPane getTxtFile2() {
	if (txtFile2 == null) {
	    txtFile2 = new JTextPane();
	    txtFile2.setForeground(SystemColor.windowBorder);
	    txtFile2.setText(root.getMessages().getString("help.file.bullets"));
	    txtFile2.setFont(ResourceLoader.getFont().deriveFont(15f));
	    txtFile2.setEditable(false);
	    txtFile2.setBackground(SystemColor.window);
	    txtFile2.setBounds(43, 203, 190, 199);
	}
	return txtFile2;
    }

    private JLabel getLblImage2() {
	if (lblImage2 == null) {
	    lblImage2 = new JLabel("");
	    lblImage2.setIcon(new ImageIcon(
		    HelpFile.class.getResource("/helpimg/help-file2.png")));
	    lblImage2.setBounds(243, 93, 307, 99);
	}
	return lblImage2;
    }

    private JTextPane getTxtFile3() {
	if (txtFile3 == null) {
	    txtFile3 = new JTextPane();
	    txtFile3.setForeground(SystemColor.textInactiveText);
	    txtFile3.setText(root.getMessages().getString("help.file.tts"));
	    txtFile3.setFont(ResourceLoader.getFont().deriveFont(15f));
	    txtFile3.setEditable(false);
	    txtFile3.setBackground(SystemColor.window);
	    txtFile3.setBounds(253, 295, 295, 107);
	}
	return txtFile3;
    }
}
