package main.java.gui.cards.app;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import main.java.gui.cards.MainWindow;
import main.java.gui.cards.help.HelpResults;
import main.java.gui.util.BusyPanel;
import main.java.util.ResourceLoader;
import main.java.util.exception.ResourceException;

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
    private JButton btnTts_Auto;
    private JPanel panel;
    private JButton btnHelp_Auto;
    private JFileChooser fileChooser;
    private JLabel lblTime;
    private JButton btnReview_Auto;
    private String sourcePath;

    // Threads
    private Thread translationTask;

    /*
     * Mnemonics
     */
    private KeyEventDispatcher keyEventDispatcher;

    public CardAuto(MainWindow root) throws ResourceException {
	setBackground(SystemColor.window);
	this.root = root;

	this.setLayout(null);
	this.add(getNorthPanel_Auto());
	this.add(getCenterPanel_Auto());
	this.add(getDownPanel_Auto());
	setBounds(100, 100, 587, 420);
    }

    public void setKeyEventDispatcher(boolean set) {
	if (!set) {
	    KeyboardFocusManager.getCurrentKeyboardFocusManager()
		    .removeKeyEventDispatcher(this.keyEventDispatcher);
	} else {
	    if (this.keyEventDispatcher == null) {
		this.keyEventDispatcher = new KeyEventDispatcher() {
		    @Override
		    public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_F1
				&& e.getID() == KeyEvent.KEY_RELEASED) {
			    // Click help button
			    getBtnHelp_Auto().doClick();
			    return true; // consume the event
			}
			return false; // allow the event to be processed
				      // normally
		    }
		};
	    }
	    KeyboardFocusManager.getCurrentKeyboardFocusManager()
		    .addKeyEventDispatcher(this.keyEventDispatcher);
	}
    }

    /**
     * Create and run the automatic translation task (new thread everytime).
     */
    public void run() {
	busyPanel.start();
	this.translationTask = createThread();
	this.translationTask.start();
    }

    public Thread createThread() {
	return new Thread(new Runnable() {
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

    public void setSourcePath(String path) {
	this.sourcePath = path;
    }

    public void stopLoading() {
	busyPanel.stop(false);
	setCursor(Cursor.getDefaultCursor());
	btnSave_Auto.setEnabled(true);
	btnTts_Auto.setEnabled(true);
	btnReview_Auto.setEnabled(true);
	lblTitle_Auto.setText(
		root.getMessages().getString("label.auto.title.success"));
	lblTime.setText(
		root.getMessages().getString("label.auto.subtitle.success"));

	// Show an error message if results are incomplete
	root.areResultsComplete();
    }

    public void reset() {
	// root.reset();
	busyPanel.stop(true);
	btnSave_Auto.setEnabled(false);
	btnTts_Auto.setEnabled(false);
	btnReview_Auto.setEnabled(false);
	lblTitle_Auto.setText(
		root.getMessages().getString("label.auto.title.loading"));
	lblTime.setText(
		root.getMessages().getString("label.auto.subtitle.loading"));
	setKeyEventDispatcher(false);
    }

    private boolean getSaveFileChooser() {
	if (root.hasDirectory()) {
	    return true;
	}

	fileChooser = new JFileChooser(this.sourcePath);
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
	    centerPanel_Automatic.add(getBusyPanel());
	    centerPanel_Automatic.add(getLblTime());
	    centerPanel_Automatic.add(getPanel());
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
	    btnSave_Auto.setBounds(371, 8, 165, 42);
	    btnSave_Auto.setMnemonic(btnSave_Auto.getText().charAt(0));
	    btnSave_Auto.setIcon(new ImageIcon(
		    CardAuto.class.getResource("/img/save-icon.png")));
	    btnSave_Auto.setEnabled(false);
	    btnSave_Auto.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    if (getSaveFileChooser()) {
			root.saveAll();
			setKeyEventDispatcher(false);
			root.show("end");
		    }
		}

	    });
	    btnSave_Auto.setMnemonic(btnSave_Auto.getText().charAt(0));
	    btnSave_Auto.setFont(ResourceLoader.getFont().deriveFont(20f));
	    btnSave_Auto.setFocusable(false);
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
	    backPanel_Auto.add(getBtnSave_Auto());
	    backPanel_Auto.add(getBtnHelp_Auto());
	}
	return backPanel_Auto;
    }

    private JLabel getLblTime() throws ResourceException {
	if (lblTime == null) {
	    lblTime = new JLabel(root.getMessages()
		    .getString("label.auto.subtitle.loading"));
	    lblTime.setForeground(Color.decode("#0089d6"));
	    lblTime.setHorizontalAlignment(SwingConstants.CENTER);
	    lblTime.setBounds(10, 108, 566, 32);
	    lblTime.setFont(ResourceLoader.getFont().deriveFont(22f));
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
	    btnReview_Auto.setMnemonic(btnReview_Auto.getText().charAt(0));
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

    private JButton getBtnTts_Auto() {
	if (btnTts_Auto == null) {
	    btnTts_Auto = new JButton(
		    root.getMessages().getString("button.tts"));
	    btnTts_Auto.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    setKeyEventDispatcher(false);
		    root.show("tts");
		}
	    });
	    btnTts_Auto.setEnabled(false);
	    btnTts_Auto.setIcon(new ImageIcon(
		    CardAuto.class.getResource("/img/texttospeech.png")));
	    btnTts_Auto.setMnemonic(btnTts_Auto.getText().charAt(0));
	    btnTts_Auto.setFont(ResourceLoader.getFont().deriveFont(20f));
	    btnTts_Auto.setBounds(306, 173, 187, 42);
	}
	return btnTts_Auto;
    }

    private JPanel getPanel() {
	if (panel == null) {
	    panel = new JPanel();
	    panel.setBackground(SystemColor.window);
	    panel.setBounds(10, 158, 566, 86);
	    panel.add(getBtnReview_Auto());
	    panel.add(getBtnTts_Auto());
	}
	return panel;
    }

    private JButton getBtnHelp_Auto() {
	if (btnHelp_Auto == null) {
	    btnHelp_Auto = new JButton("");
	    btnHelp_Auto.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    JFrame hr = new HelpResults(root);
		    hr.setVisible(true);
		}
	    });
	    btnHelp_Auto.setIcon(
		    new ImageIcon(CardAuto.class.getResource("/img/help.png")));
	    btnHelp_Auto.setToolTipText(
		    root.getMessages().getString("tooltip.auto"));
	    btnHelp_Auto.setFont(btnHelp_Auto.getFont().deriveFont(14f));
	    btnHelp_Auto.setFocusable(false);
	    btnHelp_Auto.setBorder(null);
	    btnHelp_Auto.setBackground(SystemColor.window);
	    btnHelp_Auto.setBounds(537, 8, 49, 41);
	}
	return btnHelp_Auto;
    }
}
