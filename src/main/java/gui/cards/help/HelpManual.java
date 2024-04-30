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
import main.java.util.properties.ResourceLoader;

public class HelpManual extends JFrame {

    private static final long serialVersionUID = 1L;
    private MainWindow root;
    private JLabel lblHelpManual;
    private JTextPane txtManual;
    private JLabel lblImage;
    private JLabel lblImage2;

    public HelpManual(MainWindow root) throws ResourceException {
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
	getContentPane().add(getTxtManual());
	getContentPane().add(getLblImage());
	getContentPane().add(getLblImage2());

    }

    private JLabel getLblFileLingual_1() {
	if (lblHelpManual == null) {
	    lblHelpManual = new JLabel(
		    root.getMessages().getString("help.manual.title"));
	    lblHelpManual.setHorizontalAlignment(SwingConstants.CENTER);
	    lblHelpManual.setForeground(Color.black);
	    lblHelpManual.setFont(ResourceLoader.getFont().deriveFont(40f));
	    lblHelpManual.setBounds(10, 11, 566, 61);
	}
	return lblHelpManual;
    }

    private JTextPane getTxtManual() {
	if (txtManual == null) {
	    txtManual = new JTextPane();
	    txtManual.setText(root.getMessages().getString("help.manual.text"));
	    txtManual.setFont(ResourceLoader.getFont().deriveFont(15f));
	    txtManual.setEditable(false);
	    txtManual.setForeground(SystemColor.textInactiveText);
	    txtManual.setBackground(SystemColor.window);
	    txtManual.setBounds(388, 77, 175, 311);
	}
	return txtManual;
    }

    private JLabel getLblImage() {
	if (lblImage == null) {
	    lblImage = new JLabel("");
	    lblImage.setIcon(new ImageIcon(
		    HelpManual.class.getResource("/helpimg/help-manual.png")));
	    lblImage.setBounds(10, 70, 378, 201);
	}
	return lblImage;
    }

    private JLabel getLblImage2() {
	if (lblImage2 == null) {
	    lblImage2 = new JLabel("");
	    lblImage2.setIcon(new ImageIcon(
		    HelpManual.class.getResource("/helpimg/help-manual2.png")));

	    lblImage2.setBounds(20, 278, 378, 110);
	}
	return lblImage2;
    }
}
