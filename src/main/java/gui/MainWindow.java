package main.java.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.java.utils.Utils;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JSplitPane;
import javax.swing.JButton;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	
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
	 * Create the frame.
	 */
	public MainWindow() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/main/resources/icon.png")));
		setTitle("FileLingual");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setFont(Utils.getFont());
		setBounds(100, 100, 600, 450);
		
		contentPane = new JPanel();
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getNorthPanel(), BorderLayout.NORTH);
		contentPane.add(getDownPanel(), BorderLayout.CENTER);
	}
	
	
	private JPanel getNorthPanel() {
		if (northPanel == null) {
			northPanel = new JPanel();
			northPanel.setLayout(new BorderLayout(0, 0));
			northPanel.add(getTitlePanel(), BorderLayout.NORTH);
			northPanel.add(getSloganPanel(), BorderLayout.CENTER);
		}
		return northPanel;
	}
	
	private JPanel getDownPanel() {
		if (downPanel == null) {
			downPanel = new JPanel();
			downPanel.add(getSplitPane());
		}
		return downPanel;
	}
	
	private JPanel getTitlePanel() {
		if (titlePanel == null) {
			titlePanel = new JPanel();
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
			sloganPanel.setLayout(new BorderLayout(0, 0));
			sloganPanel.add(getLblSlogan1(), BorderLayout.CENTER);
			sloganPanel.add(getLogoPanel(), BorderLayout.SOUTH);
		}
		return sloganPanel;
	}
	
	private JLabel getLblSlogan1() {
		if (lblSlogan == null) {
			lblSlogan = new JLabel("The automatic program translator");
			lblSlogan.setHorizontalAlignment(SwingConstants.CENTER);
			lblSlogan.setFont(Utils.getFont().deriveFont(30f));
		}
		return lblSlogan;
	}
	
	private JPanel getLogoPanel() {
		if (logoPanel == null) {
			logoPanel = new JPanel();
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
			leftButton.setMnemonic('s');
			leftButton.setFont(Utils.getFont().deriveFont(20f));
		}
		return leftButton;
	}
	
	private JButton getRightButton() {
		if (rightButton == null) {
			rightButton = new JButton("Learn more");
			rightButton.setMnemonic('l');
			rightButton.setFont(Utils.getFont().deriveFont(20f));
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
