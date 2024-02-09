package main.java.gui.windows;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.java.gui.MainWindow;
import main.java.utils.Utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextPane;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class InfoWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	
	/*
	 * Main Window
	 */
	private MainWindow main;
	
	/*
	 * Panels
	 */
	private JPanel contentPane;
	private JPanel northPanel;
	private JPanel downPanel;
	private JPanel descriptionPanel;
	private JPanel titlePanel;

	/*
	 * Labels
	 */
	private JLabel lblFileLingual;
	private JLabel lblEmpty;
	private JPanel unioviPanel;
	private JLabel lblUnioviLogo;
	private JLabel lblEmpty_1;
	private JLabel lblEmpty_1_1;
	private JLabel lblEmpty_2;
	private JLabel lblEmpty_2_1;
	private JLabel lblEmpty_2_1_1;
	private JPanel authorPanel;
	private JLabel lblAuthor;
	private JLabel lblEmail;
	private JLabel lblEmpty_3;
	private JLabel lblEmpty_3_1;
	
	/*
	 * Others
	 */
	private String email = "UO282798@uniovi.es";
	private JTextPane textDescription;
	private JPanel emptyPanel;
	private JPanel emptyPanel_1;
	private JPanel emptyPanel_2;
	private JPanel emptyPanel_3;
	private JPanel emptyPanel_4;
	private JPanel emptyPanel_5;
	private JPanel emptyPanel_6;
	private JPanel emptyPanel_7;
	private JButton btnBack;
	private JLabel lblBack;
	
	/**
	 * Create the frame.
	 * @param mainWindow 
	 */
	public InfoWindow(MainWindow mainWindow) {
		setResizable(false);
		this.main = mainWindow; 
		setBackground(SystemColor.window);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(InfoWindow.class.getResource("/main/resources/icon.png")));
		setTitle("FileLingual");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setFont(Utils.getFont());
		setBounds(100, 100, 600, 450);
		setLocationRelativeTo(main);

		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.window);
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getNorthPanel(), BorderLayout.NORTH);
		contentPane.add(getDownPanel(), BorderLayout.CENTER);
	}
	
	/*
	 * ##### GUI FLOW
	 */
	
	private void goBack() {
		this.main.setVisible(true); this.main.setEnabled(true);
		this.main.setLocationRelativeTo(null);
		this.dispose();
	}

	
	
	/*
	 * ##### DESIGN ELEMENTS
	 */

	
	
	private JPanel getNorthPanel() {
		if (northPanel == null) {
			northPanel = new JPanel();
			northPanel.setBackground(SystemColor.window);
			northPanel.setLayout(new BorderLayout(0, 0));
			northPanel.add(getTitlePanel(), BorderLayout.NORTH);
			northPanel.add(getDescriptionPanel(), BorderLayout.CENTER);
		}
		return northPanel;
	}
	
	private JPanel getDownPanel() {
		if (downPanel == null) {
			downPanel = new JPanel();
			downPanel.setBackground(SystemColor.window);
			downPanel.setLayout(new GridLayout(2, 4, 0, 0));
			downPanel.add(getEmptyPanel());
			downPanel.add(getEmptyPanel_1());
			downPanel.add(getEmptyPanel_2());
			downPanel.add(getEmptyPanel_3());
			downPanel.add(getEmptyPanel_4());
			downPanel.add(getEmptyPanel_5());
			downPanel.add(getEmptyPanel_6());
			downPanel.add(getEmptyPanel_7());
		}
		return downPanel;
	}
	
	private JPanel getTitlePanel() {
		if (titlePanel == null) {
			titlePanel = new JPanel();
			titlePanel.setBackground(SystemColor.window);
			titlePanel.setLayout(new BorderLayout(0, 0));
			titlePanel.add(getLblEmpty(), BorderLayout.NORTH);
			titlePanel.add(getLblFileLingual());
			titlePanel.add(getUnioviPanel(), BorderLayout.WEST);
		}
		return titlePanel;
	}
	
	private JLabel getLblFileLingual() {
		if (lblFileLingual == null) {
			lblFileLingual = new JLabel("Final Degree Project");
			lblFileLingual.setLabelFor(getLblUnioviLogo());
			lblFileLingual.setHorizontalAlignment(SwingConstants.CENTER);
			lblFileLingual.setFont(Utils.getFont().deriveFont(40f));
		}
		return lblFileLingual;
	}
	
	private JLabel getLblEmpty() {
		if (lblEmpty == null) {
			lblEmpty = new JLabel(" ");
			lblEmpty.setFont(new Font("Tahoma", Font.PLAIN, 40));
		}
		return lblEmpty;
	}
	
	private JPanel getDescriptionPanel() {
		if (descriptionPanel == null) {
			descriptionPanel = new JPanel();
			descriptionPanel.setBackground(SystemColor.window);
			descriptionPanel.setLayout(new BorderLayout(0, 0));
			descriptionPanel.add(getLblEmpty_2_1_1(), BorderLayout.NORTH);
			descriptionPanel.add(getLblEmpty_2_1(), BorderLayout.WEST);
			descriptionPanel.add(getLblEmpty_2(), BorderLayout.EAST);
			descriptionPanel.add(getTextDescription(), BorderLayout.CENTER);
			descriptionPanel.add(getAuthorPanel(), BorderLayout.SOUTH);
		}
		return descriptionPanel;
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
			lblUnioviLogo.setIcon(new ImageIcon(InfoWindow.class.getResource("/main/resources/uniovi-logo.png")));
			lblUnioviLogo.setHorizontalAlignment(SwingConstants.CENTER);
			lblUnioviLogo.setFont(Utils.getFont().deriveFont(30f));
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
			lblAuthor.setFont(Utils.getFont().deriveFont(15f));
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
								"Could not open the hyperlink. Error: " + err.getMessage(),
								"Error",JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			lblEmail.setForeground(Color.BLUE.darker());
			lblEmail.setHorizontalAlignment(SwingConstants.CENTER);
			lblEmail.setFont(Utils.getFont().deriveFont(15f));
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
			textDescription.setText("Externalized translation service for programs, \r\nusing the large language model ChatGPT 3.5 Turbo\r\nprovided by the OpenAI API");
			textDescription.setFont(Utils.getFont().deriveFont(20f));
			
			// Center text
			StyledDocument doc = textDescription.getStyledDocument();
			SimpleAttributeSet center = new SimpleAttributeSet();
			StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
			doc.setParagraphAttributes(0, doc.getLength(), center, false);
		}
		return textDescription;
	}
	private JPanel getEmptyPanel() {
		if (emptyPanel == null) {
			emptyPanel = new JPanel();
			emptyPanel.setBackground(SystemColor.window);
		}
		return emptyPanel;
	}
	private JPanel getEmptyPanel_1() {
		if (emptyPanel_1 == null) {
			emptyPanel_1 = new JPanel();
			emptyPanel_1.setBackground(SystemColor.window);
		}
		return emptyPanel_1;
	}
	private JPanel getEmptyPanel_2() {
		if (emptyPanel_2 == null) {
			emptyPanel_2 = new JPanel();
			emptyPanel_2.setBackground(SystemColor.window);
		}
		return emptyPanel_2;
	}
	private JPanel getEmptyPanel_3() {
		if (emptyPanel_3 == null) {
			emptyPanel_3 = new JPanel();
			emptyPanel_3.setBackground(SystemColor.window);
		}
		return emptyPanel_3;
	}
	private JPanel getEmptyPanel_4() {
		if (emptyPanel_4 == null) {
			emptyPanel_4 = new JPanel();
			emptyPanel_4.setBackground(SystemColor.window);
			emptyPanel_4.setLayout(null);
			emptyPanel_4.add(getLblBack());
			emptyPanel_4.add(getBtnBack());
		}
		return emptyPanel_4;
	}
	private JPanel getEmptyPanel_5() {
		if (emptyPanel_5 == null) {
			emptyPanel_5 = new JPanel();
			emptyPanel_5.setBackground(SystemColor.window);
		}
		return emptyPanel_5;
	}
	private JPanel getEmptyPanel_6() {
		if (emptyPanel_6 == null) {
			emptyPanel_6 = new JPanel();
			emptyPanel_6.setBackground(SystemColor.window);
		}
		return emptyPanel_6;
	}
	private JPanel getEmptyPanel_7() {
		if (emptyPanel_7 == null) {
			emptyPanel_7 = new JPanel();
			emptyPanel_7.setBackground(SystemColor.window);
		}
		return emptyPanel_7;
	}
	private JButton getBtnBack() {
		if (btnBack == null) {
			btnBack = new JButton("");
			btnBack.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					goBack();
				}
			});
			btnBack.setBounds(21, 21, 31, 26);
			btnBack.setSelectedIcon(new ImageIcon(InfoWindow.class.getResource("/main/resources/home-icon.png")));
			btnBack.setMnemonic('h');
			btnBack.setBackground(SystemColor.window);
			btnBack.setIcon(new ImageIcon(InfoWindow.class.getResource("/main/resources/home-icon.png")));
			btnBack.setBorder(null);
		}
		return btnBack;
	}
	private JLabel getLblBack() {
		if (lblBack == null) {
			lblBack = new JLabel("<");
			lblBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			lblBack.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					btnBack.doClick();
				}
			});
			lblBack.setBounds(10, 21, 31, 26);
			lblBack.setFont(lblBack.getFont().deriveFont(15f));
		}
		return lblBack;
	}
}
