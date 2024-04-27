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

import main.java.gui.util.BusyPanel;
import main.java.util.exception.ResourceException;
import main.java.util.properties.ResourceLoader;

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
    private JLabel lblBack_Auto;
    private JButton btnBack_Auto;

    private JFileChooser fileChooser;
    private JLabel lblTime;
    private JButton btnReview_Auto;

    // Threads
    // private Thread speechTask;
    private Thread translationTask;
    private JLabel lblTitle_Auto;

    public CardAuto(MainWindow root) throws ResourceException {
	this.root = root;

	this.setLayout(null);
	this.add(getNorthPanel_Auto());
	this.add(getCenterPanel_Auto());
	this.add(getDownPanel_Auto());
	setBounds(100, 100, 587, 420);

	// Translation task
	this.translationTask = new Thread(new Runnable() {
	    @Override
	    public void run() {
		// Task execution (captioning + translation)
		root.translate();
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Task finished, stop the busy panel
		SwingUtilities.invokeLater(new Runnable() {
		    @Override
		    public void run() {
			stopLoading();
		    }
		});
	    }
	});
    }

    public void run() {
	busyPanel.start();
	translationTask.start();
    }

    public void stopLoading() {
	busyPanel.stop(false);
	setCursor(Cursor.getDefaultCursor());
	btnSave_Auto.setEnabled(true);
	btnReview_Auto.setEnabled(true);
	lblTitle_Auto.setText(
		root.getMessages().getString("label.auto.title.success"));
	lblTime.setText(
		root.getMessages().getString("label.auto.subtitle.success"));
    }

    public void reset() {
	root.reset();
	busyPanel.stop(true);
	btnSave_Auto.setEnabled(false);
	btnReview_Auto.setEnabled(false);
	lblTitle_Auto.setText(
		root.getMessages().getString("label.auto.title.loading"));
	lblTime.setText(
		root.getMessages().getString("label.auto.subtitle.loading"));
    }

    private boolean getSaveFileChooser() {
	if (root.hasDirectory()) {
	    return true;
	}

	fileChooser = new JFileChooser("D:");
	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	int returnVal = fileChooser.showSaveDialog(this);

	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    root.to(fileChooser.getSelectedFile().getAbsolutePath());
	    return true;
	}
	return false;
    }

    private JPanel getCenterPanel_Auto() throws ResourceException {
	if (centerPanel_Automatic == null) {
	    centerPanel_Automatic = new JPanel();
	    centerPanel_Automatic.setBounds(0, 109, 586, 255);
	    centerPanel_Automatic.setLayout(null);
	    centerPanel_Automatic.setBackground(SystemColor.window);
	    centerPanel_Automatic.add(getBtnSave_Auto());
	    centerPanel_Automatic.add(getBusyPanel());
	    centerPanel_Automatic.add(getLblTime());
	    centerPanel_Automatic.add(getBtnReview_Auto());
	}
	return centerPanel_Automatic;
    }

    private BusyPanel getBusyPanel() {
	if (busyPanel == null) {
	    busyPanel = new BusyPanel();
	    busyPanel.setBounds(265, 37, 60, 60);
	}
	return busyPanel;
    }

    private JButton getBtnSave_Auto() {
	if (btnSave_Auto == null) {
	    btnSave_Auto = new JButton(
		    root.getMessages().getString("button.save"));
	    btnSave_Auto.setMnemonic('s');
	    btnSave_Auto.setIcon(new ImageIcon(
		    CardAuto.class.getResource("/img/save-icon.png")));
	    btnSave_Auto.setEnabled(false);
	    btnSave_Auto.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    if (getSaveFileChooser()) {
			root.saveAll();
			root.show("end");
		    }
		}

	    });
	    btnSave_Auto.setFont(btnSave_Auto.getFont().deriveFont(20f));
	    btnSave_Auto.setFocusable(false);
	    btnSave_Auto.setBounds(306, 172, 187, 42);
	}
	return btnSave_Auto;

    }

    private JPanel getNorthPanel_Auto() throws ResourceException {
	if (northPanel_Auto == null) {
	    northPanel_Auto = new JPanel();
	    northPanel_Auto.setBackground(SystemColor.window);
	    northPanel_Auto.setBounds(0, 0, 586, 111);
	    northPanel_Auto.setLayout(null);
	    northPanel_Auto.add(getLblTitle_Auto());
	}
	return northPanel_Auto;
    }

    private JPanel getDownPanel_Auto() throws ResourceException {
	if (downPanel_Auto == null) {
	    downPanel_Auto = new JPanel();
	    downPanel_Auto.setBounds(0, 362, 586, 56);
	    downPanel_Auto.setLayout(new GridLayout(0, 1, 0, 0));
	    downPanel_Auto.add(getBackPanel_Auto());
	}
	return downPanel_Auto;
    }

    private JPanel getBackPanel_Auto() throws ResourceException {
	if (backPanel_Auto == null) {
	    backPanel_Auto = new JPanel();
	    backPanel_Auto.setLayout(null);
	    backPanel_Auto.setBackground(SystemColor.window);
	    backPanel_Auto.add(getLblBack_Auto());
	    backPanel_Auto.add(getBtnBack_Auto());
	}
	return backPanel_Auto;
    }

    private JLabel getLblBack_Auto() throws ResourceException {
	if (lblBack_Auto == null) {
	    lblBack_Auto = new JLabel("<");
	    lblBack_Auto.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    btnBack_Auto.doClick();
		}
	    });
	    lblBack_Auto.setFont(ResourceLoader.getFont().deriveFont(15f));
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
		    reset();
		    root.show("mode");
		}
	    });
	    btnBack_Auto.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/img/home-icon.png")));
	    btnBack_Auto.setMnemonic('b');
	    btnBack_Auto.setBorder(null);
	    btnBack_Auto.setBackground(SystemColor.window);
	    btnBack_Auto.setBounds(20, 11, 31, 37);
	}
	return btnBack_Auto;
    }

    private JLabel getLblTime() throws ResourceException {
	if (lblTime == null) {
	    lblTime = new JLabel(root.getMessages()
		    .getString("label.auto.subtitle.loading"));
	    lblTime.setForeground(Color.decode("#0089d6"));
	    lblTime.setHorizontalAlignment(SwingConstants.CENTER);
	    lblTime.setBounds(10, 108, 566, 32);
	    lblTime.setFont(ResourceLoader.getFont().deriveFont(25f));
	}
	return lblTime;
    }

    private JButton getBtnReview_Auto() throws ResourceException {
	if (btnReview_Auto == null) {
	    btnReview_Auto = new JButton(
		    root.getMessages().getString("button.review"));
	    btnReview_Auto.setMnemonic('r');
	    btnReview_Auto.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    root.review();
		}
	    });
	    btnReview_Auto.setIcon(new ImageIcon(
		    CardAuto.class.getResource("/img/review-icon.png")));
	    btnReview_Auto.setFont(ResourceLoader.getFont().deriveFont(20f));
	    btnReview_Auto.setFocusable(false);
	    btnReview_Auto.setEnabled(false);
	    btnReview_Auto.setBounds(109, 173, 187, 42);
	}
	return btnReview_Auto;
    }

    private JLabel getLblTitle_Auto() throws ResourceException {
	if (lblTitle_Auto == null) {
	    lblTitle_Auto = new JLabel(
		    root.getMessages().getString("label.auto.title.loading"));
	    lblTitle_Auto.setHorizontalAlignment(SwingConstants.CENTER);
	    lblTitle_Auto.setForeground(Color.BLACK);
	    lblTitle_Auto.setFont(ResourceLoader.getFont().deriveFont(40f));
	    lblTitle_Auto.setBounds(0, 0, 586, 111);
	}
	return lblTitle_Auto;
    }
}
