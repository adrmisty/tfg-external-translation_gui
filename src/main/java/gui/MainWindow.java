package main.java.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import main.java.gui.windows.FileUpload;
import main.java.gui.windows.InfoWindow;
import main.java.utils.BusyPanel;
import main.java.utils.Utils;

/**
 * Main Window of the application, which consists of several different views
 * organised and displayed with a Card Layout, and represents the GUI of a
 * Python-based service.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version 1.0 (February 2024)
 */
public class MainWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JPanel currentCard; // Reference to the card being shown on screen
    private JPanel cardMain; // Start/Home window
    private JPanel cardInfo; // Information window
    private JPanel cardFile; // For uploading a file
    private JPanel cardMode; // For choosing a translation mode

    /**
     * --------------------------------- START PANEL
     */

    /*
     * Panels
     */
    private JPanel northPanel;
    private JPanel downPanel;
    private JSplitPane splitPane;

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
    private JPanel centerPanel_Info;
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

    /**
     * --------------------------------- FILE UPLOAD PANEL
     */

    private JPanel northPanel_File;
    private JLabel lblDragText;
    private JPanel centerPanel_File;
    private JLabel lblDrag;
    private JTextField txtFilePath;
    private JButton btnBrowse;
    private JPanel backEmptyPanel_Info;
    private JPanel backPanel_Info;

    /*
     * File drag and drop
     */
    private String filePath = "";
    private String fileName = "";
    boolean savedDroppedFile = false;
    private JFileChooser fileChooser;
    private JLabel lblDndWarning;

    /**
     * --------------------------------- TRANSLATION MODE PANEL
     */

    private JPanel northPanel_Mode;
    private JPanel downPanel_Mode;
    private JPanel centerPanel_Mode;

    private JButton btnManual_Mode;
    private JButton btnAutomatic_Mode;

    private JLabel lblChooseTranslation;
    private JComboBox<String> comboBox;
    private JLabel lblChoose;
    private JLabel lblLanguage;
    private JLabel lblStartTranslating;
    private JPanel centerPanel;
    private JLabel lblSlogan;
    private JPanel logoPanel;
    private JLabel lblForLogo;
    private JLabel lblLogo;
    private JLabel lblFileLingual;
    private JLabel lblBack;
    private JButton btnBack_Info;
    private JPanel downPanel_File;
    private JPanel backEmptyPanel_File;
    private JPanel backPanel_File;
    private JLabel lblBack_File;
    private JButton btnBack_File;
    private JButton btnNext_File;
    private JButton btnHelp_File;
    private JPanel backEmptyPanel_Mode;
    private JPanel backPanel_Mode;
    private JLabel lblBack_Mode;
    private JButton btnBack_Mode;
    private JButton btnNext_Mode;
    private JButton btnHelp_Mode;
    private JPanel cardAutomatic;
    private JPanel centerPanel_Automatic;
    private JButton btnSave_Auto;
    private JButton btnReview_Auto;
    private JLabel lblSave_Auto;
    private JPanel northPanel_Auto;
    private JLabel lblTitle_Auto;
    private JPanel downPanel_Auto;
    private JPanel backEmptyPanel_Auto;
    private JPanel backPanel_Auto;
    private JLabel lblBack_Auto;
    private JButton btnBack_Auto;
    private BusyPanel busyPanel;

    /**
     * Create the frame.
     */
    public MainWindow() {
	setBackground(SystemColor.window);
	setResizable(false);
	setAutoRequestFocus(false);
	setIconImage(Toolkit.getDefaultToolkit().getImage(
		MainWindow.class.getResource("/main/resources/icon.png")));
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

	// Card layout
	contentPane.setLayout(new CardLayout());
	contentPane.add(getCardMain());
	contentPane.add(getCardInfo());
	contentPane.add(getCardFile());
	contentPane.add(getCardMode());
	contentPane.add(getCardAutomatic());
	currentCard = cardMain;
    }

    /*
     * ##### GUI FLOW
     */

    private void show(JPanel newCard) {
	currentCard.setVisible(false);
	newCard.setVisible(true);
	currentCard = newCard;
    }

    private void saveFilePath(String path, String name) {
	filePath = path;
	fileName = name;
	txtFilePath.setText(name);
	btnNext_File.setEnabled(true);
    }

    private void resetFileValues() {
	filePath = "";
	fileName = "";
	txtFilePath.setText("");
	lblDndWarning.setVisible(false);
	btnNext_File.setEnabled(false);
    }

    /*
     * ##### CARDS
     */

    private JPanel getCardMain() {
	if (cardMain == null) {
	    cardMain = new JPanel();
	    cardMain.setVisible(true);
	    cardMain.setLayout(null);
	    cardMain.add(getNorthPanel());
	    cardMain.add(getDownPanel());
	    cardMain.add(getCenterPanel());
	}
	return cardMain;
    }

    private JPanel getCardInfo() {
	if (cardInfo == null) {
	    cardInfo = new JPanel();
	    cardInfo.setVisible(false);
	    cardInfo.setLayout(null);
	    cardInfo.add(getNorthPanel_Info());
	    cardInfo.add(getCenterPanel_Info());
	    cardInfo.add(getDownPanel_Info());
	    cardInfo.setVisible(false);
	}
	return cardInfo;
    }

    private JPanel getCardFile() {
	if (cardFile == null) {
	    cardFile = new JPanel();
	    cardFile.setVisible(false);
	    cardFile.setLayout(null);
	    cardFile.add(getNorthPanel_File());
	    cardFile.add(getCenterPanel_File());
	    cardFile.add(getDownPanel_File());
	    cardFile.setVisible(false);
	}
	return cardFile;
    }

    private JPanel getCardMode() {
	if (cardMode == null) {
	    cardMode = new JPanel();
	    cardMode.setBackground(SystemColor.window);
	    cardMode.setLayout(null);
	    cardMode.add(getNorthPanel_Mode());
	    cardMode.add(getCenterPanel_Mode());
	    cardMode.add(getDownPanel_Mode());
	}
	return cardMode;
    }

    /*
     * ##### DESIGN ELEMENTS
     */

    private JPanel getNorthPanel() {
	if (northPanel == null) {
	    northPanel = new JPanel();
	    northPanel.setBounds(0, 0, 586, 110);
	    northPanel.setBackground(SystemColor.window);
	    northPanel.setLayout(new BorderLayout(0, 0));
	    northPanel.add(getLblFileLingual(), BorderLayout.SOUTH);
	}
	return northPanel;
    }

    private JPanel getDownPanel() {
	if (downPanel == null) {
	    downPanel = new JPanel();
	    downPanel.setBounds(0, 246, 586, 167);
	    downPanel.setBackground(SystemColor.window);
	    downPanel.setLayout(null);
	    downPanel.add(getSplitPane());
	}
	return downPanel;
    }

    private JButton getLeftButton() {
	if (leftButton == null) {
	    leftButton = new JButton("Get started");
	    leftButton
		    .setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    leftButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    // Open translating window
		    show(cardFile);
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
		@Override
		public void actionPerformed(ActionEvent e) {
		    // Open information window
		    show(cardInfo);
		}
	    });
	    rightButton.setFont(Utils.getFont().deriveFont(20f));
	    rightButton.setMnemonic('l');
	    rightButton
		    .setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
	    splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		    getLeftButton(), getRightButton());
	    splitPane.setBounds(147, 30, 280, 35);
	}
	return splitPane;
    }

    /*
     * ##### DESIGN ELEMENTS (INFO)
     */

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
	    lblUnioviLogo.setIcon(new ImageIcon(InfoWindow.class
		    .getResource("/main/resources/uniovi-logo.png")));
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
				"Could not open the hyperlink. Error: "
					+ err.getMessage(),
				"Error", JOptionPane.ERROR_MESSAGE);
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
	    textDescription.setText(
		    "Externalized translation service for programs, \r\nusing the large language model ChatGPT 3.5 Turbo\r\nprovided by the OpenAI API");
	    textDescription.setFont(Utils.getFont().deriveFont(20f));

	    // Center text
	    StyledDocument doc = textDescription.getStyledDocument();
	    SimpleAttributeSet center = new SimpleAttributeSet();
	    StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
	    doc.setParagraphAttributes(0, doc.getLength(), center, false);
	}
	return textDescription;
    }

    /*
     * ##### DESIGN ELEMENTS
     */

    private JPanel getNorthPanel_File() {
	if (northPanel_File == null) {
	    northPanel_File = new JPanel();
	    northPanel_File.setBounds(0, 0, 586, 81);
	    northPanel_File.setBackground(SystemColor.window);
	    northPanel_File.setLayout(null);
	    northPanel_File.add(getLblStartTranslating());
	}
	return northPanel_File;
    }

    private JLabel getLblStartTranslating() {
	if (lblStartTranslating == null) {
	    lblStartTranslating = new JLabel("Start translating now!");
	    lblStartTranslating.setBounds(0, 0, 586, 80);
	    lblStartTranslating.setHorizontalAlignment(SwingConstants.CENTER);
	    lblStartTranslating.setFont(Utils.getFont().deriveFont(40f));
	}
	return lblStartTranslating;
    }

    private JLabel getLblDragText() {
	if (lblDragText == null) {
	    lblDragText = new JLabel("Drag and drop");
	    lblDragText.setBounds(156, 84, 282, 19);
	    lblDragText.setLabelFor(getLblDrag());
	    lblDragText.setHorizontalAlignment(SwingConstants.CENTER);
	    lblDragText.setFont(Utils.getFont().deriveFont(15f));
	}
	return lblDragText;
    }

    private JPanel getCenterPanel_File() {
	if (centerPanel_File == null) {
	    centerPanel_File = new JPanel();
	    centerPanel_File.setBackground(SystemColor.window);
	    centerPanel_File.setBounds(0, 81, 586, 235);
	    centerPanel_File.setLayout(null);
	    centerPanel_File.add(getLblDragText());
	    centerPanel_File.add(getLblDrag());
	    centerPanel_File.add(getTxtFilePath());
	    centerPanel_File.add(getBtnBrowse());
	    centerPanel_File.add(getLblDndWarning());
	}
	return centerPanel_File;
    }

    private JLabel getLblDrag() {
	if (lblDrag == null) {
	    lblDrag = new JLabel("");
	    lblDrag.setBounds(145, 11, 311, 141);
	    lblDrag.setIcon(new ImageIcon(
		    FileUpload.class.getResource("/main/resources/dnd.png")));
	    lblDrag.setToolTipText("Drag and drop a file here");

	    // Drag and drop functionality
	    new DropTarget(lblDrag, new DnDListener());
	}
	return lblDrag;
    }

    class DnDListener implements DropTargetListener {

	private Optional<String> getFileExtension(String path) {
	    return Optional.ofNullable(path).filter(f -> f.contains("."))
		    .map(f -> f.substring(path.lastIndexOf(".") + 1));
	}

	@Override
	public void drop(DropTargetDropEvent event) {
	    // Get dropped item data
	    event.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
	    savedDroppedFile = false;
	    Transferable tr = event.getTransferable();
	    DataFlavor[] formats = tr.getTransferDataFlavors();
	    boolean complete = false;
	    File f = null;

	    // Only check for files
	    for (DataFlavor flavor : formats) {
		try {
		    if (flavor.isFlavorJavaFileListType()) {
			@SuppressWarnings("unchecked")
			List<File> files = (List<File>) tr
				.getTransferData(flavor);
			f = files.get(0);
			complete = true;

			if (f != null) {
			    if (getFileExtension(f.getPath()).get()
				    .equals("properties")) {
				saveFilePath(f.getPath(), f.getName());
				savedDroppedFile = true;
			    } else {
				resetFileValues();
			    }
			}
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    event.dropComplete(complete);
		    getLblDndWarning().setVisible(!savedDroppedFile);
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
	    txtFilePath.setBounds(156, 163, 178, 30);
	    txtFilePath.addPropertyChangeListener(new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
		    if (btnNext_File != null) {
			if (!txtFilePath.getText().isBlank()) {
			    btnNext_File.setEnabled(true);
			} else {
			    btnNext_File.setEnabled(false);
			}
		    }
		}
	    });
	    txtFilePath.setHorizontalAlignment(SwingConstants.CENTER);
	    txtFilePath.setFont(Utils.getFont().deriveFont(14f));
	    txtFilePath.setEditable(false);
	    txtFilePath.setColumns(10);
	}
	return txtFilePath;
    }

    private JFileChooser getFileChooser() {
	fileChooser = new JFileChooser("D:");
	FileNameExtensionFilter filter = new FileNameExtensionFilter(
		"Internationalization files", "properties");
	fileChooser.setFileFilter(filter);

	int returnVal = fileChooser.showOpenDialog(this);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    saveFilePath(fileChooser.getSelectedFile().getPath(),
		    fileChooser.getSelectedFile().getName());
	    lblDndWarning.setVisible(false);
	}
	return fileChooser;
    }

    private JButton getBtnBrowse() {
	if (btnBrowse == null) {
	    btnBrowse = new JButton("Browse...");
	    btnBrowse.setBounds(349, 166, 89, 23);
	    btnBrowse.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    getFileChooser();
		}
	    });
	    btnBrowse.setMnemonic('b');
	    btnBrowse.setFont(Utils.getFont().deriveFont(14f));
	}
	return btnBrowse;
    }

    private JLabel getLblDndWarning() {
	if (lblDndWarning == null) {
	    lblDndWarning = new JLabel(
		    "Oops! You tried to drop a non-valid file.");
	    lblDndWarning.setForeground(new Color(220, 20, 60));
	    lblDndWarning.setBounds(156, 200, 282, 14);
	    lblDndWarning.setVisible(false);
	    lblDndWarning.setFont(Utils.getFont().deriveFont(14f));
	}
	return lblDndWarning;
    }

    private JPanel getNorthPanel_Mode() {
	if (northPanel_Mode == null) {
	    northPanel_Mode = new JPanel();
	    northPanel_Mode.setBounds(0, 0, 586, 81);
	    northPanel_Mode.setBackground(SystemColor.window);
	    northPanel_Mode.setLayout(null);
	    northPanel_Mode.add(getLblChooseTranslation());
	}
	return northPanel_Mode;
    }

    private JPanel getDownPanel_Mode() {
	if (downPanel_Mode == null) {
	    downPanel_Mode = new JPanel();
	    downPanel_Mode.setBounds(0, 315, 586, 98);
	    downPanel_Mode.setLayout(new GridLayout(0, 1, 0, 0));
	    downPanel_Mode.add(getBackEmptyPanel_Mode());
	    downPanel_Mode.add(getBackPanel_Mode());
	}
	return downPanel_Mode;
    }

    private JPanel getCenterPanel_Mode() {
	if (centerPanel_Mode == null) {
	    centerPanel_Mode = new JPanel();
	    centerPanel_Mode.setBounds(0, 80, 586, 236);
	    centerPanel_Mode.setBackground(SystemColor.window);
	    centerPanel_Mode.setLayout(null);
	    centerPanel_Mode.add(getBtnManual_Mode());
	    centerPanel_Mode.add(getBtnAutomatic_Mode());
	    centerPanel_Mode.add(getComboBox());
	    centerPanel_Mode.add(getLblChoose());
	    centerPanel_Mode.add(getLblLanguage());
	}
	return centerPanel_Mode;
    }

    private void setSelectedButton(JButton thisButton, JButton otherButton) {
	thisButton.setSelected(true);
	thisButton.setFocusable(true);
	otherButton.setSelected(false);
	otherButton.setFocusable(false);
    }

    private boolean unlockNext() {
	// Checks whether it is possible to enable the 'Next' button
	if ((btnManual_Mode.isSelected() || btnAutomatic_Mode.isSelected())
		&& comboBox.getSelectedIndex() > 0) {
	    btnNext_Mode.setEnabled(true);
	    return true;
	} else {
	    btnNext_Mode.setEnabled(false);
	    return false;
	}
    }

    private JButton getBtnManual_Mode() {
	if (btnManual_Mode == null) {
	    btnManual_Mode = new JButton("Manual");
	    btnManual_Mode.setFocusable(false);
	    btnManual_Mode.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    setSelectedButton(btnManual_Mode, btnAutomatic_Mode);
		    unlockNext();
		}
	    });
	    btnManual_Mode.setBounds(67, 77, 210, 52);
	    btnManual_Mode.setFont(Utils.getFont().deriveFont(20f));
	}
	return btnManual_Mode;
    }

    private JButton getBtnAutomatic_Mode() {
	if (btnAutomatic_Mode == null) {
	    btnAutomatic_Mode = new JButton("Automatic");
	    btnAutomatic_Mode.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    setSelectedButton(btnAutomatic_Mode, btnManual_Mode);
		    unlockNext();
		}
	    });
	    btnAutomatic_Mode.setFont(Utils.getFont().deriveFont(20f));
	    btnAutomatic_Mode.setBounds(305, 77, 210, 52);
	}
	return btnAutomatic_Mode;
    }

    private JComboBox<String> getComboBox() {
	if (comboBox == null) {
	    comboBox = new JComboBox<String>();
	    comboBox.addItemListener(new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
		    if (btnNext_Mode != null) {
			unlockNext();
		    }
		}
	    });
	    comboBox.setBounds(197, 185, 201, 32);
	    comboBox.setModel(new DefaultComboBoxModel<String>(
		    Utils.getSupportedLanguages().toArray(new String[0])));
	    comboBox.setFont(Utils.getFont().deriveFont(15f));
	}
	return comboBox;
    }

    private JLabel getLblChoose() {
	if (lblChoose == null) {
	    lblChoose = new JLabel(
		    "1) Do the translation yourself, or have AI do it!");
	    lblChoose.setForeground(SystemColor.textHighlight);
	    lblChoose.setHorizontalAlignment(SwingConstants.CENTER);
	    lblChoose.setBounds(131, 44, 327, 22);
	    lblChoose.setFont(Utils.getFont().deriveFont(15f));
	}
	return lblChoose;
    }

    private JLabel getLblLanguage() {
	if (lblLanguage == null) {
	    lblLanguage = new JLabel("2) Choose the target language!");
	    lblLanguage.setLabelFor(getComboBox());
	    lblLanguage.setHorizontalAlignment(SwingConstants.CENTER);
	    lblLanguage.setForeground(SystemColor.textHighlight);
	    lblLanguage.setFont(Utils.getFont().deriveFont(15f));
	    lblLanguage.setBounds(131, 155, 327, 19);
	}
	return lblLanguage;
    }

    private JLabel getLblChooseTranslation() {
	if (lblChooseTranslation == null) {
	    lblChooseTranslation = new JLabel("Choose your translation mode");
	    lblChooseTranslation.setBounds(0, 5, 586, 76);
	    lblChooseTranslation.setHorizontalAlignment(SwingConstants.CENTER);
	    lblChooseTranslation.setFont(Utils.getFont().deriveFont(40f));
	}
	return lblChooseTranslation;
    }

    private JPanel getCenterPanel() {
	if (centerPanel == null) {
	    centerPanel = new JPanel();
	    centerPanel.setBackground(SystemColor.window);
	    centerPanel.setBounds(0, 110, 586, 136);
	    centerPanel.setLayout(new BorderLayout(0, 0));
	    centerPanel.add(getLblSlogan(), BorderLayout.NORTH);
	    centerPanel.add(getLogoPanel());
	}
	return centerPanel;
    }

    private JLabel getLblSlogan() {
	if (lblSlogan == null) {
	    lblSlogan = new JLabel("The automatic program translator");
	    lblSlogan.setHorizontalAlignment(SwingConstants.CENTER);
	    lblSlogan.setFont(lblSlogan.getFont().deriveFont(30f));
	    lblSlogan.setBackground(SystemColor.window);
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
	    lblLogo.setIcon(new ImageIcon(MainWindow.class
		    .getResource("/main/resources/openai-logo.png")));
	    lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
	}
	return lblLogo;
    }

    private JLabel getLblFileLingual() {
	if (lblFileLingual == null) {
	    lblFileLingual = new JLabel("FileLingual");
	    lblFileLingual.setHorizontalAlignment(SwingConstants.CENTER);
	    lblFileLingual.setFont(Utils.getFont().deriveFont(50f));
	}
	return lblFileLingual;
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
	    lblBack.setFont(Utils.getFont().deriveFont(15f));
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
		    show(cardMain);
		}
	    });
	    btnBack_Info.setIcon(new ImageIcon(MainWindow.class
		    .getResource("/main/resources/home-icon.png")));
	    btnBack_Info.setMnemonic('h');
	    btnBack_Info.setBorder(null);
	    btnBack_Info.setBackground(SystemColor.window);
	    btnBack_Info.setBounds(21, 11, 31, 37);
	}
	return btnBack_Info;
    }

    private JPanel getDownPanel_File() {
	if (downPanel_File == null) {
	    downPanel_File = new JPanel();
	    downPanel_File.setBackground(SystemColor.window);
	    downPanel_File.setBounds(0, 315, 586, 103);
	    downPanel_File.setLayout(new GridLayout(2, 4, 0, 0));
	    downPanel_File.add(getBackEmptyPanel_File());
	    downPanel_File.add(getBackPanel_File());
	}
	return downPanel_File;
    }

    private JPanel getBackEmptyPanel_File() {
	if (backEmptyPanel_File == null) {
	    backEmptyPanel_File = new JPanel();
	    backEmptyPanel_File.setLayout(null);
	    backEmptyPanel_File.setBackground(SystemColor.window);
	}
	return backEmptyPanel_File;
    }

    private JPanel getBackPanel_File() {
	if (backPanel_File == null) {
	    backPanel_File = new JPanel();
	    backPanel_File.setLayout(null);
	    backPanel_File.setBackground(SystemColor.window);
	    backPanel_File.add(getLblBack_File());
	    backPanel_File.add(getBtnBack_File());
	    backPanel_File.add(getBtnNext_File());
	    backPanel_File.add(getBtnHelp_File());
	}
	return backPanel_File;
    }

    private JLabel getLblBack_File() {
	if (lblBack_File == null) {
	    lblBack_File = new JLabel("<");
	    lblBack_File.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    btnBack_File.doClick();
		}
	    });
	    lblBack_File.setFont(Utils.getFont().deriveFont(15f));
	    lblBack_File.setBounds(10, 11, 31, 37);
	}
	return lblBack_File;
    }

    private JButton getBtnBack_File() {
	if (btnBack_File == null) {
	    btnBack_File = new JButton("");
	    btnBack_File.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    show(cardMain);
		}
	    });
	    btnBack_File.setIcon(new ImageIcon(MainWindow.class
		    .getResource("/main/resources/home-icon.png")));
	    btnBack_File.setMnemonic('h');
	    btnBack_File.setBorder(null);
	    btnBack_File.setBackground(SystemColor.window);
	    btnBack_File.setBounds(20, 11, 31, 37);
	}
	return btnBack_File;
    }

    private JButton getBtnNext_File() {
	if (btnNext_File == null) {
	    btnNext_File = new JButton("Next");
	    btnNext_File.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    show(cardMode);
		}
	    });
	    btnNext_File.setMnemonic('b');
	    btnNext_File.setFont(Utils.getFont().deriveFont(14f));
	    btnNext_File.setEnabled(false);
	    btnNext_File.setBounds(448, 13, 86, 33);
	}
	return btnNext_File;
    }

    private JButton getBtnHelp_File() {
	if (btnHelp_File == null) {
	    btnHelp_File = new JButton("");
	    btnHelp_File.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		}
	    });
	    btnHelp_File.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/main/resources/help.png")));
	    btnHelp_File.setToolTipText(
		    "Drag and drop, or select the file you wish to translate - remember it must be a .properties localization file!");
	    btnHelp_File.setMnemonic('b');
	    btnHelp_File.setFont(btnHelp_File.getFont().deriveFont(14f));
	    btnHelp_File.setBorder(null);
	    btnHelp_File.setBackground(SystemColor.window);
	    btnHelp_File.setBounds(537, 7, 49, 41);
	    btnHelp_File.setFocusable(false);
	}
	return btnHelp_File;
    }

    private JPanel getBackEmptyPanel_Mode() {
	if (backEmptyPanel_Mode == null) {
	    backEmptyPanel_Mode = new JPanel();
	    backEmptyPanel_Mode.setBackground(SystemColor.window);
	}
	return backEmptyPanel_Mode;
    }

    private JPanel getBackPanel_Mode() {
	if (backPanel_Mode == null) {
	    backPanel_Mode = new JPanel();
	    backPanel_Mode.setBackground(SystemColor.window);
	    backPanel_Mode.setLayout(null);
	    backPanel_Mode.add(getLblBack_Mode());
	    backPanel_Mode.add(getBtnBack_Mode());
	    backPanel_Mode.add(getBtnNext_Mode());
	    backPanel_Mode.add(getBtnHelp_Mode());
	}
	return backPanel_Mode;
    }

    private JLabel getLblBack_Mode() {
	if (lblBack_Mode == null) {
	    lblBack_Mode = new JLabel("<");
	    lblBack_Mode.setLabelFor(getBtnBack_Mode());
	    lblBack_Mode.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    btnBack_Mode.doClick();
		}
	    });
	    lblBack_Mode.setBounds(10, 11, 31, 37);
	    lblBack_Mode.setFont(Utils.getFont().deriveFont(15f));
	}
	return lblBack_Mode;
    }

    private JButton getBtnBack_Mode() {
	if (btnBack_Mode == null) {
	    btnBack_Mode = new JButton("");
	    btnBack_Mode.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    show(cardFile);
		}
	    });
	    btnBack_Mode.setIcon(new ImageIcon(MainWindow.class
		    .getResource("/main/resources/home-icon.png")));
	    btnBack_Mode.setBounds(20, 11, 31, 37);
	    btnBack_Mode.setMnemonic('b');
	    btnBack_Mode.setBorder(null);
	    btnBack_Mode.setBackground(SystemColor.window);
	}
	return btnBack_Mode;
    }

    private JButton getBtnNext_Mode() {
	if (btnNext_Mode == null) {
	    btnNext_Mode = new JButton("Next");
	    btnNext_Mode.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    show(cardAutomatic);
		}
	    });
	    btnNext_Mode.setBounds(448, 13, 86, 33);
	    btnNext_Mode.setMnemonic('n');
	    btnNext_Mode.setFont(Utils.getFont().deriveFont(14f));
	    btnNext_Mode.setEnabled(false);
	}
	return btnNext_Mode;
    }

    private JButton getBtnHelp_Mode() {
	if (btnHelp_Mode == null) {
	    btnHelp_Mode = new JButton("");
	    btnHelp_Mode.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/main/resources/help.png")));
	    btnHelp_Mode.setBounds(537, 7, 49, 41);
	    btnHelp_Mode.setToolTipText(
		    "As a translator, you can translate the file yourself or do it with OpenAI's chat completion model.");
	    btnHelp_Mode.setFont(Utils.getFont().deriveFont(14f));
	    btnHelp_Mode.setBorder(null);
	    btnHelp_Mode.setBackground(SystemColor.window);
	    btnHelp_Mode.setFocusable(false);
	}
	return btnHelp_Mode;
    }

    private JPanel getCardAutomatic() {
	if (cardAutomatic == null) {
	    cardAutomatic = new JPanel();
	    cardAutomatic.setBackground(SystemColor.window);
	    cardAutomatic.setLayout(null);
	    cardAutomatic.add(getCenterPanel_Automatic());
	    cardAutomatic.add(getNorthPanel_Auto());
	    cardAutomatic.add(getDownPanel_Auto());
	}
	return cardAutomatic;
    }

    private JPanel getCenterPanel_Automatic() {
	if (centerPanel_Automatic == null) {
	    centerPanel_Automatic = new JPanel();
	    centerPanel_Automatic.setBounds(0, 80, 586, 236);
	    centerPanel_Automatic.setLayout(null);
	    centerPanel_Automatic.setBackground(SystemColor.window);
	    centerPanel_Automatic.add(getBusyPanel());
	    centerPanel_Automatic.add(getBtnSave_Auto());
	    centerPanel_Automatic.add(getBtnReview_Auto());
	    centerPanel_Automatic.add(getLblSave_Auto());
	}
	return centerPanel_Automatic;
    }

    private BusyPanel getBusyPanel() {
	if (busyPanel == null) {
	    busyPanel = new BusyPanel();
	    busyPanel.setLocation(260, 0);
	    busyPanel.setSize(60, 60);
	}
	return busyPanel;
    }

    private JButton getBtnSave_Auto() {
	if (btnSave_Auto == null) {
	    btnSave_Auto = new JButton("Save & finish");
	    btnSave_Auto.setIcon(new ImageIcon(MainWindow.class
		    .getResource("/main/resources/save-icon.png")));
	    btnSave_Auto.setEnabled(false);
	    btnSave_Auto.setFont(btnSave_Auto.getFont().deriveFont(20f));
	    btnSave_Auto.setFocusable(false);
	    btnSave_Auto.setBounds(74, 158, 210, 52);
	}
	return btnSave_Auto;
    }

    private JButton getBtnReview_Auto() {
	if (btnReview_Auto == null) {
	    btnReview_Auto = new JButton("Review");
	    btnReview_Auto.setEnabled(false);
	    btnReview_Auto.setFont(btnReview_Auto.getFont().deriveFont(20f));
	    btnReview_Auto.setBounds(305, 157, 210, 52);
	}
	return btnReview_Auto;
    }

    private JLabel getLblSave_Auto() {
	if (lblSave_Auto == null) {
	    lblSave_Auto = new JLabel("");
	    lblSave_Auto.setHorizontalAlignment(SwingConstants.CENTER);
	    lblSave_Auto.setForeground(SystemColor.textHighlight);
	    lblSave_Auto.setFont(lblSave_Auto.getFont().deriveFont(15f));
	    lblSave_Auto.setBounds(131, 44, 327, 22);
	}
	return lblSave_Auto;
    }

    private JPanel getNorthPanel_Auto() {
	if (northPanel_Auto == null) {
	    northPanel_Auto = new JPanel();
	    northPanel_Auto.setBackground(SystemColor.window);
	    northPanel_Auto.setBounds(0, 0, 586, 81);
	    northPanel_Auto.setLayout(null);
	    northPanel_Auto.add(getLvlProgress_Auto_1());
	}
	return northPanel_Auto;
    }

    private JLabel getLvlProgress_Auto_1() {
	if (lblTitle_Auto == null) {
	    lblTitle_Auto = new JLabel("Translating your texts...");
	    lblTitle_Auto.setBounds(0, 0, 586, 81);
	    lblTitle_Auto.setHorizontalAlignment(SwingConstants.CENTER);
	    lblTitle_Auto.setForeground(Color.BLACK);
	    lblTitle_Auto.setFont(Utils.getFont().deriveFont(40f));
	}
	return lblTitle_Auto;
    }

    private JPanel getDownPanel_Auto() {
	if (downPanel_Auto == null) {
	    downPanel_Auto = new JPanel();
	    downPanel_Auto.setBounds(0, 315, 586, 98);
	    downPanel_Auto.setLayout(new GridLayout(0, 1, 0, 0));
	    downPanel_Auto.add(getBackEmptyPanel_Auto());
	    downPanel_Auto.add(getBackPanel_Auto());
	}
	return downPanel_Auto;
    }

    private JPanel getBackEmptyPanel_Auto() {
	if (backEmptyPanel_Auto == null) {
	    backEmptyPanel_Auto = new JPanel();
	    backEmptyPanel_Auto.setBackground(SystemColor.window);
	}
	return backEmptyPanel_Auto;
    }

    private JPanel getBackPanel_Auto() {
	if (backPanel_Auto == null) {
	    backPanel_Auto = new JPanel();
	    backPanel_Auto.setLayout(null);
	    backPanel_Auto.setBackground(SystemColor.window);
	    backPanel_Auto.add(getLblBack_Auto());
	    backPanel_Auto.add(getBtnBack_Auto());
	}
	return backPanel_Auto;
    }

    private JLabel getLblBack_Auto() {
	if (lblBack_Auto == null) {
	    lblBack_Auto = new JLabel("<");
	    lblBack_Auto.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    btnBack_Auto.doClick();
		}
	    });
	    lblBack_Auto.setFont(lblBack_Auto.getFont().deriveFont(15f));
	    lblBack_Auto.setBounds(10, 11, 31, 37);
	}
	return lblBack_Auto;
    }

    private JButton getBtnBack_Auto() {
	if (btnBack_Auto == null) {
	    btnBack_Auto = new JButton("");
	    btnBack_Auto.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    show(cardMode);
		}
	    });
	    btnBack_Auto.setIcon(new ImageIcon(MainWindow.class
		    .getResource("/main/resources/home-icon.png")));
	    btnBack_Auto.setMnemonic('b');
	    btnBack_Auto.setBorder(null);
	    btnBack_Auto.setBackground(SystemColor.window);
	    btnBack_Auto.setBounds(20, 11, 31, 37);
	}
	return btnBack_Auto;
    }
}
