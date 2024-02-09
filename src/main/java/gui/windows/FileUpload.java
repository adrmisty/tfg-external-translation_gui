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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;

public class FileUpload extends JFrame {

	private static final long serialVersionUID = 1L;
	
	/*
	 * Main Window
	 */
	private MainWindow main;
	
	/*
	 * Panels
	 */
	private JPanel contentPane;
	private JPanel northPanel_File;
	private JPanel titlePanel_File;

	/*
	 * Labels
	 */
	private JLabel lblStartTranslating;
	private JLabel lblEmpty_File;
	private JFileChooser fileChooser;
	private JTextField textFileName;
	private JButton btnBrowse;
	private JLabel lblDragText;
	private JPanel panelDrag;
	private JLabel lblDrag;
	private JLabel lblNewLabel;
	
	/**
	 * Create the frame.
	 * @param mainWindow 
	 */
	public FileUpload(MainWindow mainWindow) {
		setResizable(false);
		this.main = mainWindow; 
		setBackground(SystemColor.window);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(FileUpload.class.getResource("/main/resources/icon.png")));
		setTitle("FileLingual");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setFont(Utils.getFont());
		setBounds(100, 100, 600, 450);
		setLocationRelativeTo(main);

		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.window);
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getNorthPanel_File());
		contentPane.add(getTextFileName());
		contentPane.add(getBtnBrowse());
		contentPane.add(getPanelDrag());
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{getNorthPanel_File(), getTitlePanel_File(), getLblEmpty_File(), getLblStartTranslating(), getTextFileName(), getBtnBrowse(), getPanelDrag(), getLblDrag_1()}));
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
	
	private JFileChooser getFileChooser() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser("D:");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("i18n .properties", "properties", "properties");
			fileChooser.setFileFilter(filter);
			fileChooser.setVisible(false);
			// Open the save dialog
			fileChooser.showSaveDialog(null);
		}
		return fileChooser;
	}
	private JTextField getTextFileName() {
		if (textFileName == null) {
			textFileName = new JTextField();
			textFileName.setHorizontalAlignment(SwingConstants.CENTER);
			textFileName.setEditable(false);
			textFileName.setBounds(147, 257, 178, 30);
			textFileName.setColumns(10);
			textFileName.setFont(Utils.getFont().deriveFont(14f));
		}
		return textFileName;
	}
	private JButton getBtnBrowse() {
		if (btnBrowse == null) {
			btnBrowse = new JButton("Browse...");
			btnBrowse.setMnemonic('b');
			btnBrowse.setBounds(335, 261, 89, 23);
			btnBrowse.setFont(Utils.getFont().deriveFont(14f));
		}
		return btnBrowse;
	}
	private JLabel getLblDragText() {
		if (lblDragText == null) {
			lblDragText = new JLabel("Drag and drop");
			lblDragText.setLabelFor(getLblDrag_1());
			lblDragText.setHorizontalAlignment(SwingConstants.CENTER);
			lblDragText.setBounds(10, 85, 282, 19);
			lblDragText.setFont(Utils.getFont().deriveFont(15f));
		}
		return lblDragText;
	}
	private JPanel getPanelDrag() {
		if (panelDrag == null) {
			panelDrag = new JPanel();
			panelDrag.setBackground(SystemColor.window);
			panelDrag.setBounds(135, 108, 294, 142);
			panelDrag.setLayout(null);
			panelDrag.add(getLblDragText());
			panelDrag.add(getLblDrag_1());
			panelDrag.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{getLblDragText(), getLblDrag_1()}));
		}
		return panelDrag;
	}
	private JLabel getLblDrag_1() {
		if (lblDrag == null) {
			lblDrag = new JLabel("");
			lblDrag.setBounds(0, 11, 292, 131);
			lblDrag.setIcon(new ImageIcon(FileUpload.class.getResource("/main/resources/draganddrop (1).png")));
			lblDrag.setToolTipText("Drag and drop for files");
		}
		return lblDrag;
	}
}
