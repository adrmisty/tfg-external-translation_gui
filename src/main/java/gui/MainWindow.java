package main.java.gui;

import java.awt.CardLayout;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.java.api.Translator;
import main.java.gui.cards.CardAuto;
import main.java.gui.cards.CardEnd;
import main.java.gui.cards.CardFile;
import main.java.gui.cards.CardInfo;
import main.java.gui.cards.CardMain;
import main.java.gui.cards.CardMode;
import main.java.gui.other.IDE;
import main.java.utils.ResourceLoader;

/**
 * Main Window of the application, which consists of several different views
 * organised and displayed with a Card Layout.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version 2.0 (February 2024)
 */
public class MainWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    // API access for translation
    private Translator translator = new Translator();

    // Card layout
    private JPanel contentPane;
    private JPanel currentCard; // Reference to the card being shown on screen
    private JPanel cardMain; // Start/Home window
    private CardInfo cardInfo; // Information window
    private CardFile cardFile; // For uploading a file
    private CardMode cardMode; // For choosing a translation mode
    private CardEnd cardEnd; // For ending the app
    private CardAuto cardAuto;

    // Files & translation
    private String inputFilePath = "";
    private String directoryPath = "";
    private String language = "";

    /**
     * Create the frame.
     */
    public MainWindow() {
	setBackground(SystemColor.window);
	setResizable(false);
	setAutoRequestFocus(false);
	setIconImage(Toolkit.getDefaultToolkit().getImage(
		MainWindow.class.getResource("/main/resources/img/icon.png")));
	setTitle("FileLingual");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setFont(ResourceLoader.getFont());
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
	contentPane.add(getCardAuto());
	contentPane.add(getCardEnd());
	currentCard = cardMain;
    }

    /**
     * Shows a new card in the main window.
     * 
     * @param String identificating the new card to show
     * @throws Exception if the card to show is not recognised
     */
    public void show(String newCard) {
	currentCard.setVisible(false);

	switch (newCard) {
	case "main":
	    currentCard = cardMain;
	    break;
	case "file":
	    currentCard = cardFile;
	    break;
	case "info":
	    currentCard = cardInfo;
	    break;
	case "mode":
	    currentCard = cardMode;
	    break;
	case "automatic":
	    currentCard = cardAuto;
	    cardAuto.executeAutomaticTranslation();
	    break;
	case "end":
	    currentCard = cardEnd;
	    cardEnd.setSavedFileName(translator.getSavedFileName());
	    break;
	default:
	    showErrorMessage(
		    "The selected screen to be shown is not supported.");
	}

	currentCard.setVisible(true);
    }

    /**
     * Shows an error message on the screen explaining the malfunction.
     * 
     * @param message: explanation of the error
     */
    public void showErrorMessage(String message) {
	JOptionPane.showMessageDialog(this, message, "FileLingual: error",
		JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows an error message on the screen explaining the malfunction.
     * 
     * @param additional message to add to error visualization
     * @param Exception  thrown by a lower-level function
     */
    public void showErrorMessage(Exception e, String message) {
	JOptionPane.showMessageDialog(this, message + " " + e.getMessage(),
		"FileLingual: error", JOptionPane.ERROR_MESSAGE);
    }

    public void save() throws IOException {
	translator.save(directoryPath);
    }

    public void autoTranslate() throws Exception {
	translator.translateTo(this.language);
    }

    public void review() throws IOException {
	IDE.open(contentPane, translator.review());
    }

    public void setLanguage(String language) {
	this.language = language;
    }

    public void chooseFile(String path) {
	this.inputFilePath = path;
    }

    public void chooseDirectory(String path) {
	this.directoryPath = path;
    }

    public boolean choseDirectory() {
	return !directoryPath.isBlank();
    }

    public void editFile() {
	IDE.open(contentPane,
		this.directoryPath + translator.getSavedFileName());
    }

    public void inputFile() throws Exception {
	translator.input(inputFilePath);
    }

    public void resetFileValues() {
	this.inputFilePath = "";
	this.directoryPath = "";
    }

    public void resetModeValues() {
	cardMode.reset();
    }

    /*
     * ##### CARDS
     */

    private JPanel getCardMain() {
	if (cardMain == null) {
	    cardMain = new CardMain(this);
	    cardMain.setVisible(true);
	}
	return cardMain;
    }

    private JPanel getCardInfo() {
	if (cardInfo == null) {
	    cardInfo = new CardInfo(this);
	    cardInfo.setVisible(false);
	}
	return cardInfo;
    }

    private JPanel getCardFile() {
	if (cardFile == null) {
	    cardFile = new CardFile(this);
	    cardFile.setVisible(false);
	}
	return cardFile;
    }

    private JPanel getCardMode() {
	if (cardMode == null) {
	    cardMode = new CardMode(this);
	    cardMode.setVisible(false);
	}
	return cardMode;
    }

    private JPanel getCardAuto() {
	if (cardAuto == null) {
	    cardAuto = new CardAuto(this);
	    cardAuto.setVisible(false);
	}
	return cardAuto;
    }

    private JPanel getCardEnd() {
	if (cardEnd == null) {
	    cardEnd = new CardEnd(this);
	    cardEnd.setVisible(false);
	}
	return cardEnd;
    }

}
