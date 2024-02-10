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
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
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
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

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
	private JPanel downPanel;
	private JPanel emptyPanel;
	private JPanel emptyPanel_1;
	private JPanel emptyPanel_2;
	private JPanel emptyPanel_3;
	private JPanel emptyPanel_4;
	private JLabel lblBack;
	private JButton btnBack;
	private JPanel emptyPanel_5;
	private JPanel emptyPanel_6;
	private JPanel emptyPanel_7;
	private JButton btnNext;
	private JButton btnHelp;
	
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
		contentPane.add(getPanelDrag());
		contentPane.add(getDownPanel());
	}
	
	/*
	 * ##### GUI FLOW
	 */
	
	private void goBack() {
		this.main.setVisible(true); this.main.setEnabled(true);
		this.main.setLocationRelativeTo(null);
		this.dispose();
	}
	
	private void saveFilePath(String path, String name) {
		filePath = path; fileName = name;
		txtFilePath.setText(name);
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
			panelDrag.add(getBtnBrowse_1());
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
					if (!txtFilePath.getText().isBlank()) {
						btnNext.setEnabled(true);
					} else {
						btnNext.setEnabled(false);
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
	
	private JButton getBtnBrowse_1() {
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
	private JPanel getDownPanel() {
		if (downPanel == null) {
			downPanel = new JPanel();
			downPanel.setBackground(SystemColor.window);
			downPanel.setBounds(0, 300, 586, 111);
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
			emptyPanel_4.setLayout(null);
			emptyPanel_4.setBackground(SystemColor.window);
			emptyPanel_4.add(getLblBack());
			emptyPanel_4.add(getBtnBack());
		}
		return emptyPanel_4;
	}
	private JLabel getLblBack() {
		if (lblBack == null) {
			lblBack = new JLabel("<");
			lblBack.setFont(lblBack.getFont().deriveFont(15f));
			lblBack.setBounds(10, 21, 31, 26);
		}
		return lblBack;
	}
	private JButton getBtnBack() {
		if (btnBack == null) {
			btnBack = new JButton("");
			btnBack.setIcon(new ImageIcon(FileUpload.class.getResource("/main/resources/home-icon.png")));
			btnBack.setMnemonic('h');
			btnBack.setBorder(null);
			btnBack.setBackground(SystemColor.window);
			btnBack.setBounds(21, 21, 31, 26);
		}
		return btnBack;
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
			emptyPanel_7.setLayout(null);
			emptyPanel_7.add(getBtnBrowse_1_1());
			emptyPanel_7.add(getBtnHelp());
		}
		return emptyPanel_7;
	}
	private JButton getBtnBrowse_1_1() {
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
	
}
