package main.java.gui.cards;

import java.awt.Color;
import java.awt.Cursor;
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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import main.java.gui.MainWindow;
import main.java.gui.other.BusyPanel;
import main.java.utils.ResourceLoader;

public class CardAuto extends JPanel {

    private static final long serialVersionUID = 1L;
    private MainWindow root;

    /*
     * Panels
     */
    private JPanel northPanel_Auto;
    private JPanel centerPanel_Automatic;
    private BusyPanel busyPanel;
    private JPanel downPanel_Auto;
    private JPanel backPanel_Auto;

    /*
     * Labels & text
     */
    private JButton btnSave_Auto;
    private JLabel lblTitle_Auto;
    private JPanel backEmptyPanel_Auto;
    private JLabel lblBack_Auto;
    private JButton btnBack_Auto;

    private JFileChooser fileChooser;
    private JLabel lblTime;

    public CardAuto(MainWindow root) {
	this.root = root;

	this.setLayout(null);
	this.add(getNorthPanel_Auto());
	this.add(getCenterPanel_Auto());
	this.add(getDownPanel_Auto());
    }

    public void executeAutomaticTranslation() {
	busyPanel.start();

	new Thread(new Runnable() {
	    @Override
	    public void run() {
		try {
		    // Task execution
		    root.autoTranslate();
		    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		    // Task finished, stop the busy panel
		    SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
			    stopLoading();
			}
		    });
		} catch (Exception ex) {
		    root.showErrorMessage(ex.getMessage());
		}
	    }
	}).start();

    }

    public void stopLoading() {
	busyPanel.stop(false);
	setCursor(Cursor.getDefaultCursor());
	btnSave_Auto.setEnabled(true);
	lblTitle_Auto.setText("Translation successfully completed!");
	lblTime.setText("Seems like we are done :)");
    }

    private boolean getSaveFileChooser() {
	if (root.choseDirectory()) {
	    return true;
	}

	fileChooser = new JFileChooser("D:");
	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	int returnVal = fileChooser.showSaveDialog(this);

	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    root.chooseDirectory(
		    fileChooser.getSelectedFile().getAbsolutePath());
	    return true;
	}
	return false;
    }

    private JPanel getCenterPanel_Auto() {
	if (centerPanel_Automatic == null) {
	    centerPanel_Automatic = new JPanel();
	    centerPanel_Automatic.setBounds(0, 63, 586, 253);
	    centerPanel_Automatic.setLayout(null);
	    centerPanel_Automatic.setBackground(SystemColor.window);
	    centerPanel_Automatic.add(getBtnSave_Auto());
	    centerPanel_Automatic.add(getLvlProgress_Auto_1());
	    centerPanel_Automatic.add(getBusyPanel());
	    centerPanel_Automatic.add(getLblTime());
	}
	return centerPanel_Automatic;
    }

    private BusyPanel getBusyPanel() {
	if (busyPanel == null) {
	    busyPanel = new BusyPanel();
	    busyPanel.setBounds(265, 63, 60, 60);
	}
	return busyPanel;
    }

    private JButton getBtnSave_Auto() {
	if (btnSave_Auto == null) {
	    btnSave_Auto = new JButton("Save & finish");
	    btnSave_Auto.setIcon(new ImageIcon(CardAuto.class
		    .getResource("/main/resources/img/save-icon.png")));
	    btnSave_Auto.setEnabled(false);
	    btnSave_Auto.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    if (getSaveFileChooser()) {
			try {
			    root.save();
			    root.show("end");
			} catch (Exception e1) {
			    root.showErrorMessage(e1,
				    "There has been an issue while saving the translation.");
			}
		    }
		}
	    });
	    btnSave_Auto.setFont(btnSave_Auto.getFont().deriveFont(20f));
	    btnSave_Auto.setFocusable(false);
	    btnSave_Auto.setBounds(193, 176, 210, 52);
	}
	return btnSave_Auto;
    }

    private JPanel getNorthPanel_Auto() {
	if (northPanel_Auto == null) {
	    northPanel_Auto = new JPanel();
	    northPanel_Auto.setBackground(SystemColor.window);
	    northPanel_Auto.setBounds(0, 0, 586, 65);
	    northPanel_Auto.setLayout(null);
	}
	return northPanel_Auto;
    }

    private JLabel getLvlProgress_Auto_1() {
	if (lblTitle_Auto == null) {
	    lblTitle_Auto = new JLabel("Translating your texts...");
	    lblTitle_Auto.setBounds(10, 0, 586, 52);
	    lblTitle_Auto.setHorizontalAlignment(SwingConstants.CENTER);
	    lblTitle_Auto.setForeground(Color.BLACK);
	    lblTitle_Auto.setFont(ResourceLoader.getFont().deriveFont(30f));
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
		    busyPanel.stop(true);
		    root.show("mode");
		}
	    });
	    btnBack_Auto.setIcon(new ImageIcon(MainWindow.class
		    .getResource("/main/resources/img/home-icon.png")));
	    btnBack_Auto.setMnemonic('b');
	    btnBack_Auto.setBorder(null);
	    btnBack_Auto.setBackground(SystemColor.window);
	    btnBack_Auto.setBounds(20, 11, 31, 37);
	}
	return btnBack_Auto;
    }

    private JLabel getLblTime() {
	if (lblTime == null) {
	    lblTime = new JLabel("This might take some time... Hang on!");
	    lblTime.setForeground(SystemColor.textHighlight);
	    lblTime.setHorizontalAlignment(SwingConstants.CENTER);
	    lblTime.setBounds(173, 120, 256, 32);
	    lblTime.setFont(ResourceLoader.getFont().deriveFont(15f));
	}
	return lblTime;
    }
}
