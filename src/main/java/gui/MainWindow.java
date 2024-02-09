package main.java.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.java.gui.info.InfoWindow;
import main.java.gui.translate.StartTranslatingWindow;
import main.java.utils.Utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.font.TextAttribute;
import java.util.Map;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JSplitPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	
	
	/**
	 * --------------------------------- START PANEL
	 */
	
	
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

	
	/*
	 * Window types
	 */
	private enum WindowType {INFO, TRANSLATE};
	private JFrame infoWindow;
	private JFrame translateWindow;
	
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
	
	private void openWindow(WindowType window) {
		switch(window) {
		case INFO:
			this.setEnabled(false);
			infoWindow = new InfoWindow(this);
			infoWindow.setLocationRelativeTo(this);
			infoWindow.setVisible(true);
			break;
			
		case TRANSLATE:
			this.setEnabled(false);
			translateWindow = new StartTranslatingWindow(this);
			translateWindow.setLocationRelativeTo(this);
			translateWindow.setVisible(true);
			break;
			
		default:
			break;
		}
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
					openWindow(WindowType.TRANSLATE);
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
					openWindow(WindowType.INFO);
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
}
