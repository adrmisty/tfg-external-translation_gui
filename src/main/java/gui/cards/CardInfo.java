package main.java.gui.cards;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import main.java.gui.MainWindow;
import main.java.utils.ResourceLoader;

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
    private JPanel unioviPanel;
    private JPanel authorPanel;
    private JPanel backEmptyPanel_Info;
    private JPanel backPanel_Info;

    /*
     * Labels & text
     */
    private JLabel lblProject;
    private JLabel lblEmpty_Info;
    private JLabel lblUnioviLogo;
    private JLabel lblEmpty_1;
    private JLabel lblEmpty_1_1;
    private JLabel lblEmpty_2;
    private JLabel lblEmpty_2_1;
    private JLabel lblEmpty_2_1_1;
    private JLabel lblAuthor;
    private JLabel lblEmail;
    private JLabel lblEmpty_3;
    private JLabel lblEmpty_3_1;
    private String email = "UO282798@uniovi.es";
    private JTextPane textDescription;
    private JLabel lblBack;
    private JButton btnBack_Info;

    public CardInfo(MainWindow root) {
	this.root = root;

	this.setLayout(null);
	this.add(getNorthPanel_Info());
	this.add(getCenterPanel_Info());
	this.add(getDownPanel_Info());
    }

    private JPanel getNorthPanel_Info() {
	if (northPanel_Info == null) {
	    northPanel_Info = new JPanel();
	    northPanel_Info.setBounds(0, 0, 586, 142);
	    northPanel_Info.setBackground(SystemColor.window);
	    northPanel_Info.setLayout(null);
	    northPanel_Info.add(getTitlePanel_Info());
	}
	return northPanel_Info;
    }

    private JPanel getDownPanel_Info() {
	if (downPanel_Info == null) {
	    downPanel_Info = new JPanel();
	    downPanel_Info.setBounds(0, 310, 586, 103);
	    downPanel_Info.setBackground(SystemColor.window);
	    downPanel_Info.setLayout(new GridLayout(2, 4, 0, 0));
	    downPanel_Info.add(getBackEmptyPanel_Info());
	    downPanel_Info.add(getBackPanel_Info());
	}
	return downPanel_Info;
    }

    private JPanel getTitlePanel_Info() {
	if (titlePanel_Info == null) {
	    titlePanel_Info = new JPanel();
	    titlePanel_Info.setBounds(0, 0, 586, 142);
	    titlePanel_Info.setBackground(SystemColor.window);
	    titlePanel_Info.setLayout(new BorderLayout(0, 0));
	    titlePanel_Info.add(getLblEmpty_Info(), BorderLayout.NORTH);
	    titlePanel_Info.add(getLblProject());
	    titlePanel_Info.add(getUnioviPanel(), BorderLayout.WEST);
	}
	return titlePanel_Info;
    }

    private JLabel getLblProject() {
	if (lblProject == null) {
	    lblProject = new JLabel("Final Degree Project");
	    lblProject.setLabelFor(getLblUnioviLogo());
	    lblProject.setHorizontalAlignment(SwingConstants.CENTER);
	    lblProject.setFont(ResourceLoader.getFont().deriveFont(40f));
	}
	return lblProject;
    }

    private JLabel getLblEmpty_Info() {
	if (lblEmpty_Info == null) {
	    lblEmpty_Info = new JLabel(" ");
	    lblEmpty_Info.setFont(new Font("Tahoma", Font.PLAIN, 40));
	}
	return lblEmpty_Info;
    }

    private JPanel getCenterPanel_Info() {
	if (centerPanel_Info == null) {
	    centerPanel_Info = new JPanel();
	    centerPanel_Info.setBounds(0, 142, 586, 168);
	    centerPanel_Info.setBackground(SystemColor.window);
	    centerPanel_Info.setLayout(new BorderLayout(0, 0));
	    centerPanel_Info.add(getLblEmpty_2_1_1(), BorderLayout.NORTH);
	    centerPanel_Info.add(getLblEmpty_2_1(), BorderLayout.WEST);
	    centerPanel_Info.add(getLblEmpty_2(), BorderLayout.EAST);
	    centerPanel_Info.add(getTextDescription(), BorderLayout.CENTER);
	    centerPanel_Info.add(getAuthorPanel(), BorderLayout.SOUTH);
	}
	return centerPanel_Info;
    }

    private JPanel getUnioviPanel() {
	if (unioviPanel == null) {
	    unioviPanel = new JPanel();
	    unioviPanel.setBackground(SystemColor.window);
	    unioviPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	    unioviPanel.add(getLblEmpty_1_1());
	    unioviPanel.add(getLblUnioviLogo());
	    unioviPanel.add(getLblEmpty_1());
	}
	return unioviPanel;
    }

    private JLabel getLblUnioviLogo() {
	if (lblUnioviLogo == null) {
	    lblUnioviLogo = new JLabel("");
	    lblUnioviLogo.setIcon(new ImageIcon(MainWindow.class
		    .getResource("/main/resources/img/uniovi-logo.png")));
	    lblUnioviLogo.setHorizontalAlignment(SwingConstants.CENTER);
	    lblUnioviLogo.setFont(ResourceLoader.getFont().deriveFont(30f));
	}
	return lblUnioviLogo;
    }

    private JLabel getLblEmpty_1() {
	if (lblEmpty_1 == null) {
	    lblEmpty_1 = new JLabel(" ");
	    lblEmpty_1.setFont(new Font("Tahoma", Font.PLAIN, 40));
	}
	return lblEmpty_1;
    }

    private JLabel getLblEmpty_1_1() {
	if (lblEmpty_1_1 == null) {
	    lblEmpty_1_1 = new JLabel(" ");
	    lblEmpty_1_1.setFont(new Font("Tahoma", Font.PLAIN, 40));
	}
	return lblEmpty_1_1;
    }

    private JLabel getLblEmpty_2() {
	if (lblEmpty_2 == null) {
	    lblEmpty_2 = new JLabel(" ");
	    lblEmpty_2.setFont(new Font("Tahoma", Font.PLAIN, 40));
	}
	return lblEmpty_2;
    }

    private JLabel getLblEmpty_2_1() {
	if (lblEmpty_2_1 == null) {
	    lblEmpty_2_1 = new JLabel(" ");
	    lblEmpty_2_1.setFont(new Font("Tahoma", Font.PLAIN, 40));
	}
	return lblEmpty_2_1;
    }

    private JLabel getLblEmpty_2_1_1() {
	if (lblEmpty_2_1_1 == null) {
	    lblEmpty_2_1_1 = new JLabel(" ");
	    lblEmpty_2_1_1.setBackground(SystemColor.window);
	    lblEmpty_2_1_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
	}
	return lblEmpty_2_1_1;
    }

    private JPanel getAuthorPanel() {
	if (authorPanel == null) {
	    authorPanel = new JPanel();
	    authorPanel.setBackground(SystemColor.window);
	    authorPanel.setLayout(new BorderLayout(0, 0));
	    authorPanel.add(getLblEmpty_3(), BorderLayout.NORTH);
	    authorPanel.add(getLblAuthor());
	    authorPanel.add(getLblEmpty_3_1(), BorderLayout.WEST);
	    authorPanel.add(getLblEmail(), BorderLayout.SOUTH);
	}
	return authorPanel;
    }

    private JLabel getLblAuthor() {
	if (lblAuthor == null) {
	    lblAuthor = new JLabel("Developed by Adriana Rodríguez Flórez");
	    lblAuthor.setLabelFor(getLblEmail());
	    lblAuthor.setBackground(SystemColor.window);
	    lblAuthor.setHorizontalAlignment(SwingConstants.CENTER);
	    lblAuthor.setFont(ResourceLoader.getFont().deriveFont(15f));
	}
	return lblAuthor;
    }

    private JLabel getLblEmail() {
	if (lblEmail == null) {
	    lblEmail = new JLabel("uo282798@uniovi.es");
	    lblEmail.setBackground(SystemColor.window);
	    lblEmail.setToolTipText("Send an email!");
	    lblEmail.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    try {
			String url = "mailto:" + email;
			Desktop.getDesktop().browse(new URI(url));
		    } catch (IOException | URISyntaxException err) {
			JOptionPane.showMessageDialog(e.getComponent(),
				"Could not open the hyperlink. Error: "
					+ err.getMessage(),
				"Error", JOptionPane.ERROR_MESSAGE);
		    }
		}
	    });
	    lblEmail.setForeground(Color.BLUE.darker());
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

    private JTextPane getTextDescription() {
	if (textDescription == null) {
	    textDescription = new JTextPane();
	    textDescription.setBackground(SystemColor.window);
	    textDescription.setEditable(false);
	    textDescription.setText(
		    "Externalized translation service for programs, \r\nusing the large language model ChatGPT 3.5 Turbo\r\nprovided by the OpenAI API");
	    textDescription.setFont(ResourceLoader.getFont().deriveFont(20f));

	    // Center text
	    StyledDocument doc = textDescription.getStyledDocument();
	    SimpleAttributeSet center = new SimpleAttributeSet();
	    StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
	    doc.setParagraphAttributes(0, doc.getLength(), center, false);
	}
	return textDescription;
    }

    private JPanel getBackEmptyPanel_Info() {
	if (backEmptyPanel_Info == null) {
	    backEmptyPanel_Info = new JPanel();
	    backEmptyPanel_Info.setLayout(null);
	    backEmptyPanel_Info.setBackground(SystemColor.window);
	}
	return backEmptyPanel_Info;
    }

    private JPanel getBackPanel_Info() {
	if (backPanel_Info == null) {
	    backPanel_Info = new JPanel();
	    backPanel_Info.setLayout(null);
	    backPanel_Info.setBackground(SystemColor.window);
	    backPanel_Info.add(getLblBack_Info());
	    backPanel_Info.add(getBtnBack_Info());
	}
	return backPanel_Info;
    }

    private JLabel getLblBack_Info() {
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
		    try {
			root.show("main");
		    } catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		    }
		}
	    });
	    btnBack_Info.setIcon(new ImageIcon(MainWindow.class
		    .getResource("/main/resources/img/home-icon.png")));
	    btnBack_Info.setMnemonic('h');
	    btnBack_Info.setBorder(null);
	    btnBack_Info.setBackground(SystemColor.window);
	    btnBack_Info.setBounds(21, 11, 31, 37);
	}
	return btnBack_Info;
    }

}
