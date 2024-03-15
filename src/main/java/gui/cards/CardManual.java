package main.java.gui.cards;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.java.gui.MainWindow;
import main.java.utils.ResourceLoader;

public class CardManual extends JPanel {

    private static final long serialVersionUID = 1L;
    private MainWindow root;

    /*
     * Panels
     */
    private JPanel northPanel_Manual;
    private JPanel centerPanel_Manual;
    private JPanel downPanel_Manual;
    private JPanel backPanel_Manual;

    /*
     * Labels & text
     */
    private JButton btnTranslate_Manual;
    private JLabel lblTitle_Manual;
    private JPanel backEmptyPanel_Manual;
    private JLabel lblBack_Manual;
    private JButton btnBack_Manual;

    private JFileChooser fileChooser;
    private JLabel lblLanguage;
    private JLabel lblFileLocation;
    private JButton btnNext_Manual;
    private JTextField txtPath;
    private JButton btnBrowse;
    private JTextField txtLanguage;

    private String language;

    public CardManual(MainWindow root) {
	this.root = root;

	this.setLayout(null);
	this.add(getNorthPanel_Manual());
	this.add(getCenterPanel_Auto());
	this.add(getDownPanel_Manual());
    }

    public void setLanguage(String language) {
	txtLanguage.setText(language);
    }

    public void reset() {
	btnTranslate_Manual.setEnabled(false);
	btnNext_Manual.setEnabled(false);
	txtPath.setText("");
	txtLanguage.setText("");
    }

    private boolean getSaveFileChooser() {
	if (root.choseDirectory()) {
	    return true;
	}

	fileChooser = new JFileChooser("D:");
	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	int returnVal = fileChooser.showSaveDialog(this);

	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    txtPath.setText(fileChooser.getSelectedFile().getPath());
	    root.chooseDirectory(
		    fileChooser.getSelectedFile().getAbsolutePath());
	    return true;
	}
	return false;
    }

    private JPanel getCenterPanel_Auto() {
	if (centerPanel_Manual == null) {
	    centerPanel_Manual = new JPanel();
	    centerPanel_Manual.setBounds(0, 63, 586, 253);
	    centerPanel_Manual.setLayout(null);
	    centerPanel_Manual.setBackground(SystemColor.window);
	    centerPanel_Manual.add(getBtnTranslate_Manual());
	    centerPanel_Manual.add(getLvlProgress_Auto_1());
	    centerPanel_Manual.add(getLblLanguage());
	    centerPanel_Manual.add(getLblFileLocation());
	    centerPanel_Manual.add(getTxtPath());
	    centerPanel_Manual.add(getBtnBrowse());
	    centerPanel_Manual.add(getTxtLanguage());
	}
	return centerPanel_Manual;
    }

    private JButton getBtnTranslate_Manual() {
	if (btnTranslate_Manual == null) {
	    btnTranslate_Manual = new JButton(
		    root.getMessages().getString("button.translate"));
	    btnTranslate_Manual.setIcon(new ImageIcon(CardManual.class
		    .getResource("/main/resources/img/save-icon.png")));
	    btnTranslate_Manual.setEnabled(false);
	    btnTranslate_Manual.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    try {
			root.manualTranslate();
		    } catch (Exception e1) {
			root.showErrorMessage(
				root.getMessages().getString("error.manual"));
		    }
		}
	    });
	    btnTranslate_Manual
		    .setFont(btnTranslate_Manual.getFont().deriveFont(20f));
	    btnTranslate_Manual.setFocusable(false);
	    btnTranslate_Manual.setBounds(232, 150, 153, 42);
	}
	return btnTranslate_Manual;
    }

    private JPanel getNorthPanel_Manual() {
	if (northPanel_Manual == null) {
	    northPanel_Manual = new JPanel();
	    northPanel_Manual.setBackground(SystemColor.window);
	    northPanel_Manual.setBounds(0, 0, 586, 65);
	    northPanel_Manual.setLayout(null);
	}
	return northPanel_Manual;
    }

    private JLabel getLvlProgress_Auto_1() {
	if (lblTitle_Manual == null) {
	    lblTitle_Manual = new JLabel(
		    root.getMessages().getString("label.manual.title"));
	    lblTitle_Manual.setBounds(10, 0, 586, 52);
	    lblTitle_Manual.setHorizontalAlignment(SwingConstants.CENTER);
	    lblTitle_Manual.setForeground(Color.BLACK);
	    lblTitle_Manual.setFont(ResourceLoader.getFont().deriveFont(30f));
	}
	return lblTitle_Manual;
    }

    private JPanel getDownPanel_Manual() {
	if (downPanel_Manual == null) {
	    downPanel_Manual = new JPanel();
	    downPanel_Manual.setBounds(0, 315, 586, 98);
	    downPanel_Manual.setLayout(new GridLayout(0, 1, 0, 0));
	    downPanel_Manual.add(getBackEmptyPanel_Manual());
	    downPanel_Manual.add(getBackPanel_Manual());
	}
	return downPanel_Manual;
    }

    private JPanel getBackEmptyPanel_Manual() {
	if (backEmptyPanel_Manual == null) {
	    backEmptyPanel_Manual = new JPanel();
	    backEmptyPanel_Manual.setBackground(SystemColor.window);
	}
	return backEmptyPanel_Manual;
    }

    private JPanel getBackPanel_Manual() {
	if (backPanel_Manual == null) {
	    backPanel_Manual = new JPanel();
	    backPanel_Manual.setLayout(null);
	    backPanel_Manual.setBackground(SystemColor.window);
	    backPanel_Manual.add(getLblBack_Manual());
	    backPanel_Manual.add(getBtnBack_Manual());
	    backPanel_Manual.add(getBtnNext_Manual());
	}
	return backPanel_Manual;
    }

    private JLabel getLblBack_Manual() {
	if (lblBack_Manual == null) {
	    lblBack_Manual = new JLabel("<");
	    lblBack_Manual.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    btnBack_Manual.doClick();
		}
	    });
	    lblBack_Manual.setFont(lblBack_Manual.getFont().deriveFont(15f));
	    lblBack_Manual.setBounds(10, 11, 31, 37);
	}
	return lblBack_Manual;
    }

    private JButton getBtnBack_Manual() {
	if (btnBack_Manual == null) {
	    btnBack_Manual = new JButton("");
	    btnBack_Manual.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    reset();
		    root.show("mode");
		}
	    });
	    btnBack_Manual.setIcon(new ImageIcon(MainWindow.class
		    .getResource("/main/resources/img/home-icon.png")));
	    btnBack_Manual.setMnemonic('b');
	    btnBack_Manual.setBorder(null);
	    btnBack_Manual.setBackground(SystemColor.window);
	    btnBack_Manual.setBounds(20, 11, 31, 37);
	}
	return btnBack_Manual;
    }

    private JLabel getLblLanguage() {
	if (lblLanguage == null) {
	    lblLanguage = new JLabel(
		    root.getMessages().getString("label.manual.language"));
	    lblLanguage.setForeground(SystemColor.textHighlight);
	    lblLanguage.setHorizontalAlignment(SwingConstants.CENTER);
	    lblLanguage.setBounds(46, 63, 199, 32);
	    lblLanguage.setFont(ResourceLoader.getFont().deriveFont(15f));
	}
	return lblLanguage;
    }

    private JLabel getLblFileLocation() {
	if (lblFileLocation == null) {
	    lblFileLocation = new JLabel(
		    root.getMessages().getString("label.manual.file"));
	    lblFileLocation.setHorizontalAlignment(SwingConstants.CENTER);
	    lblFileLocation.setForeground(SystemColor.textHighlight);
	    lblFileLocation.setFont(ResourceLoader.getFont().deriveFont(15f));
	    lblFileLocation.setBounds(46, 98, 199, 32);
	}
	return lblFileLocation;
    }

    private JButton getBtnNext_Manual() {
	if (btnNext_Manual == null) {
	    btnNext_Manual = new JButton(
		    root.getMessages().getString("button.next"));
	    btnNext_Manual.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    root.show("end");
		}
	    });
	    btnNext_Manual.setMnemonic('b');
	    btnNext_Manual.setFont(ResourceLoader.getFont().deriveFont(14f));
	    btnNext_Manual.setEnabled(false);
	    btnNext_Manual.setBounds(448, 13, 86, 33);
	}
	return btnNext_Manual;
    }

    private JTextField getTxtPath() {
	if (txtPath == null) {
	    txtPath = new JTextField();
	    txtPath.setHorizontalAlignment(SwingConstants.CENTER);
	    txtPath.setFont(ResourceLoader.getFont().deriveFont(14f));
	    txtPath.setEditable(false);
	    txtPath.setColumns(10);
	    txtPath.setBounds(222, 100, 178, 30);
	}
	return txtPath;
    }

    private JButton getBtnBrowse() {
	if (btnBrowse == null) {
	    btnBrowse = new JButton(
		    root.getMessages().getString("button.browse"));
	    btnBrowse.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    if (getSaveFileChooser()) {
			btnTranslate_Manual.setEnabled(true);
			btnNext_Manual.setEnabled(true);
		    }
		}
	    });
	    btnBrowse.setMnemonic('b');
	    btnBrowse.setFont(btnBrowse.getFont().deriveFont(14f));
	    btnBrowse.setBounds(410, 103, 89, 23);
	}
	return btnBrowse;
    }

    private JTextField getTxtLanguage() {
	if (txtLanguage == null) {
	    txtLanguage = new JTextField(this.language);
	    txtLanguage.setHorizontalAlignment(SwingConstants.CENTER);
	    txtLanguage.setFont(ResourceLoader.getFont().deriveFont(14f));
	    txtLanguage.setEditable(false);
	    txtLanguage.setColumns(10);
	    txtLanguage.setBounds(222, 63, 178, 30);
	}
	return txtLanguage;
    }

}
