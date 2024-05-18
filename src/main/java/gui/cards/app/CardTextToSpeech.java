package main.java.gui.cards.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import main.java.gui.cards.MainWindow;
import main.java.gui.cards.help.HelpTTS;
import main.java.logic.file.TargetFile;
import main.java.util.exception.ResourceException;
import main.java.util.properties.ResourceLoader;

public class CardTextToSpeech extends JPanel {

    private static final long serialVersionUID = 1L;
    private MainWindow root;

    /*
     * Panels
     */
    private JPanel northPanel_AutoTTS;
    private JPanel downPanel_AutoTTS;
    private JPanel centerPanel_TTS;
    private JLabel lblTTS;
    private JLabel lblChoose;
    private JLabel lblReading;
    private JPanel backPanel_AutoTTS;
    private JLabel lblBack_TTS;
    private JButton btnBack_TTS;
    private JButton btnHelp_TTS;
    private JToggleButton btnTTS;

    /*
     * Buttons
     */
    private List<TargetFile> targets = new ArrayList<>();
    private ButtonGroup buttonGroup;
    private List<AbstractButton> buttons = new ArrayList<>();
    private TargetFile currentFile = null;

    /*
     * Text to speech
     */
    private Thread speechTask;
    private boolean runSpeech;
    private JPanel panel;

    public CardTextToSpeech(MainWindow root) throws ResourceException {
	this.root = root;

	this.setBackground(SystemColor.window);
	this.setLayout(null);
	this.add(getDownPanel_AutoTTS());
	this.add(getNorthPanel_AutoTTS());
	setButtonGroup();
	this.add(getCenterPanel_TTS());
	// Text-to-speech for source file
	this.speechTask = createThread();
    }

    /**
     * ########## GUI FLOW ########## - Resetting values - Resetting parameters
     */

    public Thread createThread() {
	return new Thread(new Runnable() {
	    @Override
	    public void run() {
		while (runSpeech) {
		    setReadingStatus(true);
		    root.readTargetFile(currentFile, true);
		    runSpeech = false;
		}
		root.readInputFile(false);
		setReadingStatus(false);
	    }
	});
    }

    private void setReadingStatus(boolean reading) {
	if (reading) {
	    lblReading.setVisible(true);
	    changeTtsButton();
	} else {
	    lblReading.setVisible(false);
	    btnTTS.setText(root.getMessages().getString("button.tts"));
	    btnTTS.setSelected(false);
	    btnTTS.setForeground(Color.BLACK);

	    // Recreate the thread
	    this.speechTask = createThread();
	}
    }

    public void reset() throws ResourceException {
	for (AbstractButton b : buttons) {
	    buttonGroup.remove(b);
	    centerPanel_TTS.remove(b);
	}
	KeyboardFocusManager.getCurrentKeyboardFocusManager()
		.addKeyEventDispatcher(new KeyEventDispatcher() {
		    @Override
		    public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_F1
				&& e.getID() == KeyEvent.KEY_RELEASED) {
			    // Click help button
			    getBtnHelp_TTS().doClick();
			    return true; // consume the event
			}
			return false; // allow the event to be processed
				      // normally
		    }
		});
	buttons = new ArrayList<>();
    }

    private void changeTtsButton() {
	getBtnTts().setText(root.getMessages().getString("label.stop.reading"));
	getBtnTts().setForeground(Color.decode("#0089d6"));
    }

    /**
     * Sets the languages that the user has chosen for automatic translation
     * purposes, and creates the respective radio buttons in this card.
     * 
     * @param languages list of language-country strings
     * @throws ResourceException
     */
    public void setTargetFiles(List<TargetFile> files) {
	this.targets = files;
	addButtons();
	repaint();
	revalidate();
    }

    /**
     * ########## GUI ELEMENTS ##########
     */

    private JLabel getLblReading() throws ResourceException {
	if (lblReading == null) {
	    lblReading = new JLabel(
		    root.getMessages().getString("label.reading"));
	    lblReading.setBounds(10, 182, 566, 25);
	    lblReading.setHorizontalAlignment(SwingConstants.CENTER);
	    lblReading.setForeground(Color.decode("#0089d6"));
	    lblReading.setVisible(false);
	    lblReading.setFont(ResourceLoader.getFont().deriveFont(14f));
	}
	return lblReading;
    }

    private JPanel getNorthPanel_AutoTTS() throws ResourceException {
	if (northPanel_AutoTTS == null) {
	    northPanel_AutoTTS = new JPanel();
	    northPanel_AutoTTS.setBounds(0, 0, 586, 111);
	    northPanel_AutoTTS.setBackground(SystemColor.window);
	    northPanel_AutoTTS.setLayout(null);
	    northPanel_AutoTTS.add(getLblTTS());
	}
	return northPanel_AutoTTS;
    }

    private JPanel getDownPanel_AutoTTS() throws ResourceException {
	if (downPanel_AutoTTS == null) {
	    downPanel_AutoTTS = new JPanel();
	    downPanel_AutoTTS.setBounds(0, 362, 586, 56);
	    downPanel_AutoTTS.setLayout(new BorderLayout(0, 0));
	    downPanel_AutoTTS.add(getBackPanel_AutoTTS());
	}
	return downPanel_AutoTTS;
    }

    private JPanel getCenterPanel_TTS() throws ResourceException {
	if (centerPanel_TTS == null) {
	    centerPanel_TTS = new JPanel();
	    centerPanel_TTS.setBounds(0, 110, 586, 254);
	    centerPanel_TTS.setBackground(SystemColor.window);
	    centerPanel_TTS.setLayout(null);
	    centerPanel_TTS.add(getLblChoose());
	    centerPanel_TTS.add(getPanel());
	    centerPanel_TTS.add(getLblReading());
	    setButtonGroup();
	}
	return centerPanel_TTS;
    }

    private List<AbstractButton> addButtons() throws ResourceException {
	reset();
	// For aesthetics
	int y = 50;
	int SEP = 40;
	boolean first = true;

	for (TargetFile file : targets) {
	    JRadioButton radioButton = new JRadioButton(file.getFileName());

	    radioButton.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
		    currentFile = file;
		}

	    });

	    boolean enabled = root.isSpeechAvailableFor(file.getTargetCode());
	    radioButton.setEnabled(enabled);
	    if (enabled) {
		getBtnTts().setEnabled(true);
		if (first) {
		    radioButton.setSelected(true);
		    currentFile = file;
		    first = false;
		}
	    }

	    // Select the first
	    radioButton.setHorizontalAlignment(SwingConstants.LEFT);
	    radioButton.setBackground(SystemColor.window);
	    radioButton.setBounds(200, y, 250, 30);
	    radioButton.setFont(ResourceLoader.getFont().deriveFont(14f));
	    y += SEP;
	    centerPanel_TTS.add(radioButton);
	    buttonGroup.add(radioButton);
	    buttons.add(radioButton);
	}

	return buttons;
    }

    private JLabel getLblChoose() throws ResourceException {
	if (lblChoose == null) {
	    lblChoose = new JLabel(
		    root.getMessages().getString("label.choose.read"));
	    lblChoose.setForeground(Color.decode("#0089d6"));
	    lblChoose.setHorizontalAlignment(SwingConstants.CENTER);
	    lblChoose.setBounds(10, 11, 566, 22);
	    lblChoose.setFont(ResourceLoader.getFont().deriveFont(18f));
	}
	return lblChoose;
    }

    private void setButtonGroup() throws ResourceException {
	this.buttonGroup = new ButtonGroup();
    }

    private JLabel getLblTTS() throws ResourceException {
	if (lblTTS == null) {
	    lblTTS = new JLabel(
		    root.getMessages().getString("label.tts.title"));
	    lblTTS.setBounds(0, 0, 586, 111);
	    lblTTS.setHorizontalAlignment(SwingConstants.CENTER);
	    lblTTS.setFont(ResourceLoader.getFont().deriveFont(40f));
	}
	return lblTTS;
    }

    private JPanel getBackPanel_AutoTTS() throws ResourceException {
	if (backPanel_AutoTTS == null) {
	    backPanel_AutoTTS = new JPanel();
	    backPanel_AutoTTS.setBackground(SystemColor.window);
	    backPanel_AutoTTS.setLayout(null);
	    backPanel_AutoTTS.add(getLblBack_TTS());
	    backPanel_AutoTTS.add(getBtnBack_TTS());
	    backPanel_AutoTTS.add(getBtnHelp_TTS());
	}
	return backPanel_AutoTTS;
    }

    private JLabel getLblBack_TTS() throws ResourceException {
	if (lblBack_TTS == null) {
	    lblBack_TTS = new JLabel("<");
	    lblBack_TTS.setLabelFor(getBtnBack_TTS());
	    lblBack_TTS.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    btnBack_TTS.doClick();
		}
	    });
	    lblBack_TTS.setBounds(10, 11, 31, 37);
	    lblBack_TTS.setFont(ResourceLoader.getFont().deriveFont(15f));
	}
	return lblBack_TTS;
    }

    private JButton getBtnBack_TTS() {
	if (btnBack_TTS == null) {
	    btnBack_TTS = new JButton("");
	    btnBack_TTS.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    reset();
		    root.show("auto-2");
		}
	    });
	    btnBack_TTS.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/img/home-icon.png")));
	    btnBack_TTS.setBounds(20, 11, 31, 37);
	    btnBack_TTS.setMnemonic('b');
	    btnBack_TTS.setBorder(null);
	    btnBack_TTS.setBackground(SystemColor.window);
	}
	return btnBack_TTS;
    }

    private JButton getBtnHelp_TTS() throws ResourceException {
	if (btnHelp_TTS == null) {
	    btnHelp_TTS = new JButton("");
	    btnHelp_TTS.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    JFrame ht = new HelpTTS(root);
		    ht.setVisible(true);
		}
	    });
	    btnHelp_TTS.setIcon(new ImageIcon(
		    MainWindow.class.getResource("/img/help.png")));
	    btnHelp_TTS.setBounds(537, 7, 49, 41);
	    btnHelp_TTS.setToolTipText(
		    root.getMessages().getString("tooltip.TTS"));
	    btnHelp_TTS.setFont(ResourceLoader.getFont().deriveFont(14f));
	    btnHelp_TTS.setBorder(null);
	    btnHelp_TTS.setBackground(SystemColor.window);
	    btnHelp_TTS.setFocusable(false);
	}
	return btnHelp_TTS;
    }

    private JToggleButton getBtnTts() {
	if (btnTTS == null) {
	    btnTTS = new JToggleButton(
		    root.getMessages().getString("button.tts"));
	    btnTTS.setEnabled(false);
	    btnTTS.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    AbstractButton abstractButton = (AbstractButton) e
			    .getSource();
		    boolean selected = abstractButton.getModel().isSelected();
		    if (selected) {
			runSpeech = true;
			speechTask.start();
		    } else {
			runSpeech = false;
			speechTask.interrupt();
			root.readInputFile(false);
			setReadingStatus(false);
		    }
		}
	    });
	    btnTTS.setMnemonic('t');
	    btnTTS.setFont(ResourceLoader.getFont().deriveFont(14f));
	}
	return btnTTS;
    }

    private JPanel getPanel() {
	if (panel == null) {
	    panel = new JPanel();
	    panel.setBackground(SystemColor.window);
	    panel.setBounds(10, 206, 566, 37);
	    panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	    panel.add(getBtnTts());
	}
	return panel;
    }
}
