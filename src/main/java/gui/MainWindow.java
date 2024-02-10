package main.java.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.java.gui.windows.FileUpload;
import main.java.gui.windows.InfoWindow;
import main.java.utils.Utils;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.font.TextAttribute;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.ImageIcon;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	
	
	/**
	 * --------------------------------- START PANEL
	 */
	private JPanel contentPane;
	private JPanel currentCard;
	private JPanel cardMain;
	private JPanel cardInfo;
	private JPanel cardFileUpload;
	
	/*
	 * Panels
	 */
	private JPanel northPanel;
	private JPanel downPanel;
	private JPanel sloganPanel;
	private JPanel logoPanel;
	private JSplitPane splitPane;
	private JPanel titlePanel;

	/*
	 * Labels
	 */
	private JLabel lblFileLingual;
	private JLabel lblEmpty;
	private JLabel lblSlogan;
	private JLabel lblForLogo;
	private JLabel lblLogo;
	
	/*
	 * Buttons
	 */
	private JButton leftButton;
	private JButton rightButton;

	
	/**
	 * --------------------------------- INFO PANEL
	 */

	/*
	 * Panels
	 */
	private JPanel northPanel_Info;
	private JPanel downPanel_Info;
	private JPanel descriptionPanel;
	private JPanel titlePanel_Info;

	/*
	 * Labels
	 */
	private JLabel lblProject;
	private JLabel lblEmpty_Info;
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
	 * --------------------------------- FILE UPLOAD PANEL
	 */

	private JPanel northPanel_File;
	private JPanel titlePanel_File;
	private JPanel downPanel_file;
	
	/*
	 * Labels
	 */
	private JLabel lblStartTranslating;
	private JLabel lblEmpty_File;
	private JLabel lblDragText;
	private JPanel panelDrag;
	private JLabel lblDrag;
	private JTextField txtFilePath;
	private JButton btnBrowse;
	
	/*
	 * File drag and drop
	 */
	private String filePath;
	private String fileName;
	private JFileChooser fileChooser;
	private JButton btnNext;
	private JButton btnHelp;
	private JPanel emptyPanel_file;
	private JPanel emptyPanel_1_file;
	private JPanel emptyPanel_2_file;
	private JPanel emptyPanel_3_file;
	private JPanel emptyPanel_4_file;
	private JPanel emptyPanel_5_file;
	private JPanel emptyPanel_6_file;
	private JButton btnBack_file;
	private JLabel lblBack_file;
	private JPanel emptyPanel_7_file;

	
		
	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setResizable(false);
		setAutoRequestFocus(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/main/resources/icon.png")));
		setTitle("FileLingual");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setFont(Utils.getFont());
		setBounds(100, 100, 600, 450);
		this.setLocationRelativeTo(null);
		
		// Card container
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.window);
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout());
		contentPane.add(getCardMain()); contentPane.add(getCardInfo());
		contentPane.add(getCardFileUpload());
		currentCard = cardMain;
	}
	
	
	/*
	 * ##### GUI FLOW
	 */
		
	private void moveCards(JPanel newCard) {
		currentCard.setVisible(false);
		newCard.setVisible(true);
		currentCard = newCard;
	}
	
	private void saveFilePath(String path, String name) {
		filePath = path; fileName = name;
		txtFilePath.setText(name);
	}

	
	/*
	 * ##### CARDS
	 */
	
	private JPanel getCardMain() {
		if (cardMain == null) {
			cardMain = new JPanel();
			cardMain.setLayout(new BorderLayout(0, 0));
			cardMain.add(getNorthPanel(), BorderLayout.NORTH);
			cardMain.add(getDownPanel(), BorderLayout.CENTER);
			cardMain.setVisible(true);
		}
		return cardMain;
	}

	private JPanel getCardInfo() {
		if (cardInfo == null) {
			cardInfo = new JPanel();
			cardInfo.setVisible(false);
			cardInfo.setLayout(new BorderLayout(0, 0));
			cardInfo.add(getNorthPanel_Info(), BorderLayout.NORTH);
			cardInfo.add(getDownPanel_Info(), BorderLayout.SOUTH);
			cardInfo.setVisible(false);
		}
		return cardInfo;
	}
	
	private JPanel getCardFileUpload() {
		if (cardFileUpload == null) {
			cardFileUpload = new JPanel();
			cardFileUpload.setVisible(false);
			cardFileUpload.setLayout(new BorderLayout(0, 0));
			cardFileUpload.add(getNorthPanel_File(), BorderLayout.NORTH);
			cardFileUpload.add(getPanelDrag(), BorderLayout.NORTH);
			cardFileUpload.add(getDownPanel_File(), BorderLayout.CENTER);
			cardFileUpload.setVisible(false);
		}
		return cardFileUpload;
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
			northPanel.add(getSloganPanel(), BorderLayout.CENTER);
		}
		return northPanel;
	}
	
	private JPanel getDownPanel() {
		if (downPanel == null) {
			downPanel = new JPanel();
			downPanel.setBackground(SystemColor.window);
			downPanel.add(getSplitPane());
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
		}
		return titlePanel;
	}
	
	private JLabel getLblFileLingual() {
		if (lblFileLingual == null) {
			lblFileLingual = new JLabel("FileLingual");
			lblFileLingual.setHorizontalAlignment(SwingConstants.CENTER);
			lblFileLingual.setFont(Utils.getFont().deriveFont(50f));
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
	
	private JPanel getSloganPanel() {
		if (sloganPanel == null) {
			sloganPanel = new JPanel();
			sloganPanel.setBackground(SystemColor.window);
			sloganPanel.setLayout(new BorderLayout(0, 0));
			sloganPanel.add(getLblSlogan1(), BorderLayout.CENTER);
			sloganPanel.add(getLogoPanel(), BorderLayout.SOUTH);
		}
		return sloganPanel;
	}
	
	private JLabel getLblSlogan1() {
		if (lblSlogan == null) {
			lblSlogan = new JLabel("The automatic program translator");
			lblSlogan.setBackground(SystemColor.window);
			lblSlogan.setHorizontalAlignment(SwingConstants.CENTER);
			lblSlogan.setFont(Utils.getFont().deriveFont(30f));
		}
		return lblSlogan;
	}
	
	private JPanel getLogoPanel() {
		if (logoPanel == null) {
			logoPanel = new JPanel();
			logoPanel.setBackground(SystemColor.window);
			logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			logoPanel.add(getLblForLogo());
			logoPanel.add(getLblLogo());
		}
		return logoPanel;
	}
	
	private JLabel getLblForLogo() {
		if (lblForLogo == null) {
			lblForLogo = new JLabel("Powered by");
			lblForLogo.setFont(Utils.getFont().deriveFont(20f));
		}
		return lblForLogo;
	}
	
	private JLabel getLblLogo() {
		if (lblLogo == null) {
			lblLogo = new JLabel("");
			lblLogo.setIcon(new ImageIcon(MainWindow.class.getResource("/main/resources/openai-logo.png")));
			lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblLogo;
	}
	
	private JButton getLeftButton() {
		if (leftButton == null) {
			leftButton = new JButton("Get started");
			leftButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			leftButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Open translating window
					moveCards(cardFileUpload);
				}
			});
			leftButton.setMnemonic('s');
			leftButton.setFont(Utils.getFont().deriveFont(20f));
		}
		return leftButton;
	}
	
	@SuppressWarnings("unchecked")
	private JButton getRightButton() {
		if (rightButton == null) {
			// Underlined text
			rightButton = new JButton("Learn more");
			rightButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Open information window
					moveCards(cardInfo);
				}
			});
			rightButton.setFont(Utils.getFont().deriveFont(20f));
			rightButton.setMnemonic('l');
			rightButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			Font font = rightButton.getFont();
	        @SuppressWarnings("rawtypes")
			Map attributes = font.getAttributes();
	        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
	        rightButton.setFont(font.deriveFont(attributes));		
	    }
		return rightButton;
	}

	
	private JSplitPane getSplitPane() {
		if (splitPane == null) {
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, getLeftButton(), getRightButton());
		}
		return splitPane;
	}
	
	

	
	/*
	 * ##### DESIGN ELEMENTS (INFO)
	 */

	
	
	private JPanel getNorthPanel_Info() {
		if (northPanel_Info == null) {
			northPanel_Info = new JPanel();
			northPanel_Info.setBackground(SystemColor.window);
			northPanel_Info.setLayout(new BorderLayout(0, 0));
			northPanel_Info.add(getTitlePanel_Info(), BorderLayout.NORTH);
			northPanel_Info.add(getDescriptionPanel(), BorderLayout.CENTER);
		}
		return northPanel_Info;
	}	
	private JPanel getDownPanel_Info() {
		if (downPanel_Info == null) {
			downPanel_Info = new JPanel();
			downPanel_Info.setBackground(SystemColor.window);
			downPanel_Info.setLayout(new GridLayout(2, 4, 0, 0));
			downPanel_Info.add(getEmptyPanel());
			downPanel_Info.add(getEmptyPanel_1());
			downPanel_Info.add(getEmptyPanel_2());
			downPanel_Info.add(getEmptyPanel_3());
			downPanel_Info.add(getEmptyPanel_4());
			downPanel_Info.add(getEmptyPanel_5());
			downPanel_Info.add(getEmptyPanel_6());
			downPanel_Info.add(getEmptyPanel_7());
		}
		return downPanel_Info;
	}
	private JPanel getTitlePanel_Info() {
		if (titlePanel_Info == null) {
			titlePanel_Info = new JPanel();
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
			lblProject.setFont(Utils.getFont().deriveFont(40f));
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
					moveCards(cardMain);
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
	
				
	/*
	 * ##### DESIGN ELEMENTS
	 */
	
	
	
	private JPanel getNorthPanel_File() {
		if (northPanel_File == null) {
			northPanel_File = new JPanel();
			northPanel_File.setBounds(0, 0, 586, 98);
			northPanel_File.setBackground(SystemColor.window);
			northPanel_File.setLayout(new BorderLayout(0, 0));
			northPanel_File.add(getTitlePanel_File(), BorderLayout.NORTH);
		}
		return northPanel_File;
	}
	
	private JPanel getTitlePanel_File() {
		if (titlePanel_File == null) {
			titlePanel_File = new JPanel();
			titlePanel_File.setBackground(SystemColor.window);
			titlePanel_File.setLayout(new BorderLayout(0, 0));
			titlePanel_File.add(getLblEmpty_File(), BorderLayout.NORTH);
			titlePanel_File.add(getLblStartTranslating());
		}
		return titlePanel_File;
	}
	
	private JLabel getLblStartTranslating() {
		if (lblStartTranslating == null) {
			lblStartTranslating = new JLabel("Start translating now!");
			lblStartTranslating.setHorizontalAlignment(SwingConstants.CENTER);
			lblStartTranslating.setFont(Utils.getFont().deriveFont(40f));
		}
		return lblStartTranslating;
	}
	
	private JLabel getLblEmpty_File() {
		if (lblEmpty_File == null) {
			lblEmpty_File = new JLabel(" ");
			lblEmpty_File.setFont(new Font("Tahoma", Font.PLAIN, 40));
		}
		return lblEmpty_File;
	}
	
	private JLabel getLblDragText() {
		if (lblDragText == null) {
			lblDragText = new JLabel("Drag and drop");
			lblDragText.setLabelFor(getLblDrag_1());
			lblDragText.setHorizontalAlignment(SwingConstants.CENTER);
			lblDragText.setBounds(30, 85, 282, 19);
			lblDragText.setFont(Utils.getFont().deriveFont(15f));
		}
		return lblDragText;
	}
	
	private JPanel getPanelDrag() {
		if (panelDrag == null) {
			panelDrag = new JPanel();
			panelDrag.setBackground(SystemColor.window);
			panelDrag.setBounds(117, 108, 354, 181);
			panelDrag.setLayout(null);
			panelDrag.add(getLblDragText());
			panelDrag.add(getLblDrag_1());
			panelDrag.add(getTxtFilePath());
			panelDrag.add(getBtnBrowse());
		}
		return panelDrag;
	}
	
	private JLabel getLblDrag_1() {
		if (lblDrag == null) {
			lblDrag = new JLabel("");
			lblDrag.setBounds(20, 0, 311, 141);
			lblDrag.setIcon(new ImageIcon(FileUpload.class.getResource("/main/resources/dnd.png")));
			lblDrag.setToolTipText("Drag and drop a file here");
			
			// Drag and drop functionality
			new DropTarget(lblDrag, new DnDListener());
		}
		return lblDrag;
	}
	
	class DnDListener implements DropTargetListener {
		
		@Override
		public void drop(DropTargetDropEvent event) {
			// Copies
			event.acceptDrop(1); // Action copy
			
			// Get dropped item data
			Transferable tr = event.getTransferable();
			DataFlavor[] formats = tr.getTransferDataFlavors();
			boolean complete = false;
			File f = null;
			
			// Only check for files
			for (DataFlavor flavor: formats) {
				try {
					if (flavor.isFlavorJavaFileListType()) {
						@SuppressWarnings("unchecked")
						List<File> files = (List<File>) tr.getTransferData(flavor);
						f = files.get(0);
						complete = true; 
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (f != null) {
						saveFilePath(f.getPath(), f.getName());
					}
					event.dropComplete(complete);
				}
			}
		}
	
		@Override
		public void dragEnter(DropTargetDragEvent dtde) {			
		}
	
		@Override
		public void dragOver(DropTargetDragEvent dtde) {			
		}
	
		@Override
		public void dropActionChanged(DropTargetDragEvent dtde) {			
		}
	
		@Override
		public void dragExit(DropTargetEvent dte) {			
		}
	}
	
	private JTextField getTxtFilePath() {
		if (txtFilePath == null) {
			txtFilePath = new JTextField();
			txtFilePath.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					if (btnNext != null) {
						if (!txtFilePath.getText().isBlank()) {
							btnNext.setEnabled(true);
						} else {
							btnNext.setEnabled(false);
						}
					}
				}
			});
			txtFilePath.setHorizontalAlignment(SwingConstants.CENTER);
			txtFilePath.setFont(txtFilePath.getFont().deriveFont(14f));
			txtFilePath.setEditable(false);
			txtFilePath.setColumns(10);
			txtFilePath.setBounds(30, 140, 178, 30);
		}
		return txtFilePath;
	}
	
	private JFileChooser getFileChooser() {
	    fileChooser = new JFileChooser("D:");
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "Internationalization files", "properties");
	    fileChooser.setFileFilter(filter);
	    
	    int returnVal = fileChooser.showOpenDialog(this);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       saveFilePath(fileChooser.getSelectedFile().getPath(), fileChooser.getSelectedFile().getName());
	    }
	    return fileChooser;
	}
	
	private JButton getBtnBrowse() {
		if (btnBrowse == null) {
			btnBrowse = new JButton("Browse...");
			btnBrowse.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getFileChooser();
				}
			});
			btnBrowse.setMnemonic('b');
			btnBrowse.setFont(btnBrowse.getFont().deriveFont(14f));
			btnBrowse.setBounds(222, 147, 89, 23);
		}
		return btnBrowse;
	}
	
	private JButton getBtnNext() {
		if (btnNext == null) {
			btnNext = new JButton("Next");
			btnNext.setEnabled(false);
			btnNext.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
			btnNext.setBounds(38, 11, 61, 33);
			btnNext.setMnemonic('b');
			btnNext.setFont(Utils.getFont().deriveFont(14f));
		}
		return btnNext;
	}
	
	private JButton getBtnHelp() {
			if (btnHelp == null) {
				btnHelp = new JButton("");
				btnHelp.setToolTipText("Drag and drop, or select the file you wish to translate - remember it must be a .properties localization file!");
				btnHelp.setBounds(103, 11, 33, 33);
				btnHelp.setBackground(SystemColor.window);
				btnHelp.setIcon(new ImageIcon(FileUpload.class.getResource("/main/resources/help.png")));
				btnHelp.setMnemonic('b');
				btnHelp.setFont(Utils.getFont().deriveFont(14f));
				
				btnHelp.setBorder(null);
			}
			return btnHelp;
		}

	private JPanel getDownPanel_File() {
			if (downPanel_file == null) {
				downPanel_file = new JPanel();
				downPanel_file.setBackground(SystemColor.window);
				downPanel_file.setBounds(0, 300, 586, 111);
				downPanel_file.setLayout(new GridLayout(2, 4, 0, 0));
				downPanel_file.add(getEmptyPanel_file());
				downPanel_file.add(getEmptyPanel_1_file());
				downPanel_file.add(getEmptyPanel_2_file());
				downPanel_file.add(getEmptyPanel_3_file());
				downPanel_file.add(getEmptyPanel_4_file());
				downPanel_file.add(getEmptyPanel_5_file());
				downPanel_file.add(getEmptyPanel_6_file());
				downPanel_file.add(getEmptyPanel_7_file());
			}
			return downPanel_file;
		}
		
	private JPanel getEmptyPanel_file() {
		if (emptyPanel_file == null) {
			emptyPanel_file = new JPanel();
			emptyPanel_file.setBackground(SystemColor.window);
		}
		return emptyPanel_file;
	}
	private JPanel getEmptyPanel_1_file() {
		if (emptyPanel_1_file == null) {
			emptyPanel_1_file = new JPanel();
			emptyPanel_1_file.setBackground(SystemColor.window);
		}
		return emptyPanel_1_file;
	}
	private JPanel getEmptyPanel_2_file() {
		if (emptyPanel_2_file == null) {
			emptyPanel_2_file = new JPanel();
			emptyPanel_2_file.setBackground(SystemColor.window);
		}
		return emptyPanel_2_file;
	}
	private JPanel getEmptyPanel_3_file() {
		if (emptyPanel_3_file == null) {
			emptyPanel_3_file = new JPanel();
			emptyPanel_3_file.setBackground(SystemColor.window);
		}
		return emptyPanel_3_file;
	}
	private JPanel getEmptyPanel_4_file() {
		if (emptyPanel_4_file == null) {
			emptyPanel_4_file = new JPanel();
			emptyPanel_4_file.setBackground(SystemColor.window);
			emptyPanel_4_file.setLayout(null);
			emptyPanel_4_file.add(getLblBack());
			emptyPanel_4_file.add(getBtnBack());
		}
		return emptyPanel_4;
	}
	private JPanel getEmptyPanel_5_file() {
		if (emptyPanel_5_file == null) {
			emptyPanel_5_file = new JPanel();
			emptyPanel_5_file.setBackground(SystemColor.window);
		}
		return emptyPanel_5_file;
	}
	private JPanel getEmptyPanel_6_file() {
		if (emptyPanel_6_file == null) {
			emptyPanel_6_file = new JPanel();
			emptyPanel_6_file.setBackground(SystemColor.window);
		}
		return emptyPanel_6_file;
	}
	
	private JButton getBtnBack_file() {
		if (btnBack_file == null) {
			btnBack_file = new JButton("");
			btnBack_file.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					moveCards(cardMain);
				}
			});
			btnBack_file.setBounds(21, 21, 31, 26);
			btnBack_file.setSelectedIcon(new ImageIcon(InfoWindow.class.getResource("/main/resources/home-icon.png")));
			btnBack_file.setMnemonic('h');
			btnBack_file.setBackground(SystemColor.window);
			btnBack_file.setIcon(new ImageIcon(InfoWindow.class.getResource("/main/resources/home-icon.png")));
			btnBack_file.setBorder(null);
		}
		return btnBack_file;
	}
	
	private JLabel getLblBack_file() {
		if (lblBack_file == null) {
			lblBack_file = new JLabel("<");
			lblBack_file.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			lblBack_file.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					btnBack_file.doClick();
				}
			});
			lblBack_file.setBounds(10, 21, 31, 26);
			lblBack_file.setFont(lblBack.getFont().deriveFont(15f));
		}
		return lblBack_file;
	}

	
	private JPanel getEmptyPanel_7_file() {
			if (emptyPanel_7_file == null) {
				emptyPanel_7_file = new JPanel();
				emptyPanel_7_file.setBackground(SystemColor.window);
				emptyPanel_7_file.setLayout(null);
				emptyPanel_7_file.add(getBtnNext());
				emptyPanel_7_file.add(getBtnHelp());
			}
			return emptyPanel_7_file;
		}

}
