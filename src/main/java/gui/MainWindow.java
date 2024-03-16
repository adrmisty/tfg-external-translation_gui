package main.java.gui;

import java.awt.CardLayout;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.java.api.Translator;
import main.java.file.ResourceLoader;
import main.java.gui.cards.CardAuto;
import main.java.gui.cards.CardEnd;
import main.java.gui.cards.CardFile;
import main.java.gui.cards.CardInfo;
import main.java.gui.cards.CardMain;
import main.java.gui.cards.CardManual;
import main.java.gui.cards.CardMode;
import main.java.gui.other.IDE;

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

    // Locale
    private ResourceBundle messages;

    // Card layout
    private JPanel contentPane;
    private JPanel currentCard; // Reference to the card being shown on screen
    private JPanel cardMain; // Start/Home window
    private CardInfo cardInfo; // Information window
    private CardFile cardFile; // For uploading a file
    private CardMode cardMode; // For choosing a translation mode
    private CardAuto cardAuto; // For OpenAI translation
    private CardManual cardManual; // For manual translation
    private CardEnd cardEnd; // For ending the app

    // Files & translation
    private String inputFilePath = "";
    private String directoryPath = "";
    private String language = "";
    private int languageIndex;

    /**
     * Create the frame.
     */
    public MainWindow() {
	localize(Locale.getDefault());

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
	contentPane.add(getCardManual());
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
	case "manual":
	    currentCard = cardManual;
	    cardManual.setLanguage(this.language);
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
	translator.translateTo(this.languageIndex);
    }

    public void manualTranslate() throws Exception {
	IDE.open(contentPane, translator.manualTranslateTo(this.languageIndex,
		directoryPath));
    }

    public void review() throws IOException {
	IDE.open(contentPane, translator.review());
    }

    public void setLanguage(String language, int index) {
	this.language = language;
	this.languageIndex = index;
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

    public void localize(Locale locale) {
	this.messages = ResourceBundle.getBundle("Messages", locale);
    }

    public ResourceBundle getMessages() {
	return this.messages;
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

    private JPanel getCardManual() {
	if (cardManual == null) {
	    cardManual = new CardManual(this);
	    cardManual.setVisible(false);
	}
	return cardManual;
    }

    private JPanel getCardEnd() {
	if (cardEnd == null) {
	    cardEnd = new CardEnd(this);
	    cardEnd.setVisible(false);
	}
	return cardEnd;
    }

}
