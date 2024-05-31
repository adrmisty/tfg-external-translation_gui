package main.java.gui.cards.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import main.java.gui.cards.MainWindow;
import main.java.util.exception.ResourceException;
import main.java.util.resources.ResourceLoader;

public class CardInfo extends JPanel {

    private static final long serialVersionUID = 1L;
    private MainWindow root;

    /*
     * Panels
     */
    private JPanel northPanel_Info;
    private JPanel downPanel_Info;
    private JPanel centerPanel_Info;
    private JPanel titlePanel_Info;
    private JPanel authorPanel;
    private JPanel backPanel_Info;

    /*
     * Labels & text
     */
    private JTextArea lblProject;
    private JLabel lblUnioviLogo;
    private JLabel lblAuthor;
    private JLabel lblEmail;
    private JLabel lblEmpty_3;
    private JLabel lblEmpty_3_1;
    private String email = "UO282798@uniovi.es";
    private JTextPane textDescription;
    private JLabel lblBack;
    private JButton btnBack_Info;

    public CardInfo(MainWindow root) throws ResourceException {
	setBackground(SystemColor.window);
	this.root = root;

	this.setLayout(null);
	this.add(getNorthPanel_Info());
	this.add(getCenterPanel_Info());
	add(getDownPanel_Info());
    }

    private JPanel getNorthPanel_Info() throws ResourceException {
	if (northPanel_Info == null) {
	    northPanel_Info = new JPanel();
	    northPanel_Info.setBounds(0, 0, 586, 142);
	    northPanel_Info.setBackground(SystemColor.window);
	    northPanel_Info.setLayout(null);
	    northPanel_Info.add(getTitlePanel_Info());
	}
	return northPanel_Info;
    }

    private JPanel getDownPanel_Info() throws ResourceException {
	if (downPanel_Info == null) {
	    downPanel_Info = new JPanel();
	    downPanel_Info.setBounds(0, 358, 586, 56);
	    downPanel_Info.setBackground(SystemColor.window);
	    downPanel_Info.setLayout(new BorderLayout(0, 0));
	    downPanel_Info.add(getBackPanel_Info());
	}
	return downPanel_Info;
    }

    private JPanel getTitlePanel_Info() throws ResourceException {
	if (titlePanel_Info == null) {
	    titlePanel_Info = new JPanel();
	    titlePanel_Info.setBounds(0, 0, 586, 142);
	    titlePanel_Info.setBackground(SystemColor.window);
	    titlePanel_Info.setLayout(null);
	    titlePanel_Info.add(getLblProject());
	    titlePanel_Info.add(getLblUnioviLogo());
	}
	return titlePanel_Info;
    }

    private JPanel getCenterPanel_Info() throws ResourceException {
	if (centerPanel_Info == null) {
	    centerPanel_Info = new JPanel();
	    centerPanel_Info.setBounds(0, 142, 586, 218);
	    centerPanel_Info.setBackground(SystemColor.window);
	    centerPanel_Info.setLayout(null);
	    centerPanel_Info.add(getTextDescription());
	    centerPanel_Info.add(getAuthorPanel());
	}
	return centerPanel_Info;
    }

    private JLabel getLblUnioviLogo() throws ResourceException {
	if (lblUnioviLogo == null) {
	    lblUnioviLogo = new JLabel("");
	    lblUnioviLogo.setBounds(38, 33, 113, 83);
	    lblUnioviLogo.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/img/uniovi-logo.png")));
	    lblUnioviLogo.setHorizontalAlignment(SwingConstants.CENTER);
	    lblUnioviLogo.setFont(ResourceLoader.getFont().deriveFont(30f));
	}
	return lblUnioviLogo;
    }

    private JPanel getAuthorPanel() throws ResourceException {
	if (authorPanel == null) {
	    authorPanel = new JPanel();
	    authorPanel.setBounds(0, 133, 586, 51);
	    authorPanel.setBackground(SystemColor.window);
	    authorPanel.setLayout(new BorderLayout(0, 0));
	    authorPanel.add(getLblEmpty_3(), BorderLayout.NORTH);
	    authorPanel.add(getLblAuthor());
	    authorPanel.add(getLblEmpty_3_1(), BorderLayout.WEST);
	    authorPanel.add(getLblEmail(), BorderLayout.SOUTH);
	}
	return authorPanel;
    }

    private JLabel getLblAuthor() throws ResourceException {
	if (lblAuthor == null) {
	    lblAuthor = new JLabel(
		    root.getMessages().getString("label.tfg.author"));
	    lblAuthor.setLabelFor(getLblEmail());
	    lblAuthor.setBackground(SystemColor.window);
	    lblAuthor.setHorizontalAlignment(SwingConstants.CENTER);
	    lblAuthor.setFont(ResourceLoader.getFont().deriveFont(15f));
	}
	return lblAuthor;
    }

    private JLabel getLblEmail() throws ResourceException {
	if (lblEmail == null) {
	    lblEmail = new JLabel("uo282798@uniovi.es");
	    lblEmail.setBackground(SystemColor.window);
	    lblEmail.setToolTipText(
		    root.getMessages().getString("label.tfg.email"));
	    lblEmail.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    try {
			String url = "mailto:" + email;
			Desktop.getDesktop().browse(new URI(url));
		    } catch (IOException | URISyntaxException err) {
			root.showErrorMessage(new Exception(
				root.getMessages().getString("error.email")),
				false);
		    }
		}
	    });
	    lblEmail.setForeground(Color.decode("#0089d6"));
	    lblEmail.setHorizontalAlignment(SwingConstants.CENTER);
	    lblEmail.setFont(ResourceLoader.getFont().deriveFont(15f));
	    lblEmail.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	return lblEmail;
    }

    private JLabel getLblEmpty_3() {
	if (lblEmpty_3 == null) {
	    lblEmpty_3 = new JLabel(" ");
	    lblEmpty_3.setBackground(SystemColor.window);
	    lblEmpty_3.setFont(new Font("Tahoma", Font.PLAIN, 10));
	}
	return lblEmpty_3;
    }

    private JLabel getLblEmpty_3_1() {
	if (lblEmpty_3_1 == null) {
	    lblEmpty_3_1 = new JLabel(" ");
	    lblEmpty_3_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
	}
	return lblEmpty_3_1;
    }

    private JTextPane getTextDescription() throws ResourceException {
	if (textDescription == null) {
	    textDescription = new JTextPane();
	    textDescription.setBounds(13, 25, 560, 116);
	    textDescription.setBackground(SystemColor.window);
	    textDescription.setEditable(false);
	    textDescription.setText(
		    root.getMessages().getString("label.tfg.description"));
	    textDescription.setFont(ResourceLoader.getFont().deriveFont(20f));

	    // Center text
	    StyledDocument doc = textDescription.getStyledDocument();
	    SimpleAttributeSet center = new SimpleAttributeSet();
	    StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
	    doc.setParagraphAttributes(0, doc.getLength(), center, false);
	}
	return textDescription;
    }

    private JPanel getBackPanel_Info() throws ResourceException {
	if (backPanel_Info == null) {
	    backPanel_Info = new JPanel();
	    backPanel_Info.setLayout(null);
	    backPanel_Info.setBackground(SystemColor.window);
	    backPanel_Info.add(getLblBack_Info());
	    backPanel_Info.add(getBtnBack_Info());
	}
	return backPanel_Info;
    }

    private JLabel getLblBack_Info() throws ResourceException {
	if (lblBack == null) {
	    lblBack = new JLabel("<");
	    lblBack.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    btnBack_Info.doClick();
		}
	    });
	    lblBack.setFont(ResourceLoader.getFont().deriveFont(15f));
	    lblBack.setBounds(10, 11, 31, 37);
	}
	return lblBack;
    }

    private JButton getBtnBack_Info() {
	if (btnBack_Info == null) {
	    btnBack_Info = new JButton("");
	    btnBack_Info.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    root.show("main");
		}
	    });
	    btnBack_Info.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/img/home-icon.png")));
	    btnBack_Info.setMnemonic('b');
	    btnBack_Info.setBorder(null);
	    btnBack_Info.setBackground(SystemColor.window);
	    btnBack_Info.setBounds(21, 11, 31, 37);
	    btnBack_Info.setToolTipText(
		    root.getMessages().getString("tooltip.back"));
	}
	return btnBack_Info;
    }

    private JTextArea getLblProject() throws ResourceException {
	if (lblProject == null) {
	    lblProject = new JTextArea(
		    root.getMessages().getString("label.tfg"));
	    lblProject.setBackground(SystemColor.window);
	    lblProject.setEditable(false);
	    lblProject.setLineWrap(true);
	    lblProject.setRows(2);
	    lblProject.setWrapStyleWord(true);
	    lblProject.setBounds(180, 48, 406, 94);
	    lblProject.setFont(ResourceLoader.getFont().deriveFont(40f));
	}
	return lblProject;
    }
}
