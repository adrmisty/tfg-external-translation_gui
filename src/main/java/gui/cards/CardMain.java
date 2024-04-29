package main.java.gui.cards;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.SwingConstants;

import main.java.util.exception.ResourceException;
import main.java.util.properties.ResourceLoader;

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
    private JButton translateBtn;
    private JButton infoBtn;

    /*
     * Labels
     */
    private JLabel lblSlogan;
    private JLabel lblForLogo;
    private JLabel lblLogo;
    private JLabel lblFileLingual;
    private JLabel lblLogo2;
    private JPanel pnBtns;

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
	    downPanel.setBounds(0, 308, 586, 105);
	    downPanel.setBackground(SystemColor.window);
	    downPanel.setLayout(null);
	    downPanel.add(getPnBtns());
	}
	return downPanel;
    }

    private JButton getTranslateBtn() throws ResourceException {
	if (translateBtn == null) {
	    getPnBtns().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	    translateBtn = new JButton(
		    root.getMessages().getString("button.start"));
	    translateBtn.setLocation(173, 61);
	    translateBtn.setSize(144, 33);
	    translateBtn
		    .setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    pnBtns.add(getTranslateBtn());
	    translateBtn.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    root.show("file");
		}
	    });
	    translateBtn.setMnemonic('s');
	    translateBtn.setFont(ResourceLoader.getFont().deriveFont(20f));
	}
	return translateBtn;
    }

    @SuppressWarnings("unchecked")
    private JButton getInfoBtn() throws ResourceException {
	if (infoBtn == null) {
	    // Underlined text
	    infoBtn = new JButton(root.getMessages().getString("button.learn"));
	    infoBtn.setBounds(347, 43, 144, 33);
	    infoBtn.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    // Open information window
		    root.show("info");
		}
	    });
	    infoBtn.setFont(ResourceLoader.getFont().deriveFont(20f));
	    infoBtn.setMnemonic('m');
	    infoBtn.setDisplayedMnemonicIndex(0);
	    infoBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    Font font = infoBtn.getFont();
	    Map<TextAttribute, Integer> attributes = (Map<TextAttribute, Integer>) font
		    .getAttributes();
	    attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
	    infoBtn.setFont(font.deriveFont(attributes));
	}
	return infoBtn;
    }

    private JPanel getCenterPanel() throws ResourceException {
	if (centerPanel == null) {
	    centerPanel = new JPanel();
	    centerPanel.setBackground(SystemColor.window);
	    centerPanel.setBounds(0, 110, 586, 200);
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
	    logoPanel.setLayout(null);
	    logoPanel.add(getLblForLogo());
	    logoPanel.add(getLblLogo());
	    logoPanel.add(getLblLogo2());
	}
	return logoPanel;
    }

    private JLabel getLblForLogo() throws ResourceException {
	if (lblForLogo == null) {
	    lblForLogo = new JLabel(
		    root.getMessages().getString("label.poweredby"));
	    lblForLogo.setLabelFor(getLblLogo());
	    lblForLogo.setForeground(new Color(0, 0, 0));
	    lblForLogo.setHorizontalAlignment(SwingConstants.RIGHT);
	    lblForLogo.setBounds(0, 0, 250, 170);
	    lblForLogo.setFont(ResourceLoader.getFont().deriveFont(20f));
	}
	return lblForLogo;
    }

    private JLabel getLblLogo() {
	if (lblLogo == null) {
	    lblLogo = new JLabel("");
	    lblLogo.setBounds(288, 75, 132, 45);
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

    private JLabel getLblLogo2() {
	if (lblLogo2 == null) {
	    lblLogo2 = new JLabel("");
	    lblLogo2.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/img/azure.png")));
	    lblLogo2.setHorizontalAlignment(SwingConstants.CENTER);
	    lblLogo2.setBounds(278, 29, 144, 61);
	}
	return lblLogo2;
    }

    private JPanel getPnBtns() {
	if (pnBtns == null) {
	    pnBtns = new JPanel();
	    pnBtns.setBackground(SystemColor.window);
	    pnBtns.setBounds(10, 0, 566, 42);
	    pnBtns.add(getTranslateBtn());
	    pnBtns.add(getInfoBtn());
	}
	return pnBtns;
    }
}
