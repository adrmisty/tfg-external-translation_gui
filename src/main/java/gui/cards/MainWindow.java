package main.java.gui.cards;

import java.awt.CardLayout;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.java.gui.util.IDE;
import main.java.gui.util.NumberedJMenuItem;
import main.java.logic.image.Vision;
import main.java.logic.translation.Translator;
import main.java.logic.util.exception.ImageException;
import main.java.logic.util.exception.ResourceException;
import main.java.logic.util.properties.ResourceLoader;

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
    private Translator translator;

    // Image description
    private Vision vision = new Vision();

    // Locale
    private ResourceBundle messages;

    // Card layout
    private JPanel contentPane;
    private JPanel currentCard; // Reference to the card being shown on screen
    private JPanel cardMain; // Start/Home window
    private CardInfo cardInfo; // Information window
    private CardFile cardFile; // For uploading a file
    private CardImage cardImage; // For uploading images to caption
    private CardMode cardMode; // For choosing a translation mode
    private CardAutoMode cardAutoMode; // For automatic translation settings
    private CardAuto cardAuto; // For automatic translation
    private CardManual cardManual; // For manual translation
    private CardEnd cardEnd; // For ending the app

    // Files & translation
    private boolean manualMode = false;
    private List<String> languages = new ArrayList<>();

    /*
     * Language setting
     */
    private JMenuBar menuBar;
    private JMenu mnLanguage;

    /**
     * Create the frame.
     * 
     * @
     */
    public MainWindow() {
	setIconImage(Toolkit.getDefaultToolkit()
		.getImage(MainWindow.class.getResource("/img/icon.png")));

	setBackground(SystemColor.window);
	setResizable(false);
	setAutoRequestFocus(false);
	setTitle("FileLingual");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setFont(ResourceLoader.getFont());
	setBounds(100, 100, 600, 450);
	this.setLocationRelativeTo(null);

	// Card layout
	contentPane = new JPanel();
	contentPane.setBackground(SystemColor.window);
	contentPane.setBorder(null);
	setContentPane(contentPane);
	contentPane.setLayout(new CardLayout());

	initWindow(Locale.getDefault());
    }

    /**
     * Initializes the window's components to a specific Locale.
     * 
     * @param locale specified by user or, in case of first launch, system's
     *               default @ in case of localization error
     */
    public void initWindow(Locale locale) {
	contentPane.removeAll();
	cardMain = null;
	cardInfo = null;
	cardFile = null;
	cardMode = null;
	cardAuto = null;
	cardAutoMode = null;
	cardManual = null;
	cardEnd = null;
	mnLanguage = null;
	menuBar = null;

	localize(locale);

	// Card container
	contentPane.add(getCardMain());
	contentPane.add(getCardInfo());
	contentPane.add(getCardFile());
	contentPane.add(getCardImage());
	contentPane.add(getCardMode());
	contentPane.add(getCardAutoMode());
	contentPane.add(getCardAuto());
	contentPane.add(getCardManual());
	contentPane.add(getCardEnd());
	getMnLanguage();

	currentCard = cardMain;
	validate();
	repaint();
    }

    /**
     * Shows a new card in the main window.
     * 
     * @param String identificating the new card to show @ if the card to show
     * is not recognised
     */
    public void show(String newCard) {
	currentCard.setVisible(false);

	switch (newCard) {
	case "main":
	    currentCard = cardMain;
	    mnLanguage.setEnabled(true);
	    mnLanguage.setVisible(true);
	    break;
	case "file":
	    currentCard = cardFile;
	    mnLanguage.setVisible(false);
	    break;
	case "info":
	    currentCard = cardInfo;
	    mnLanguage.setVisible(false);
	    break;
	case "image":
	    currentCard = cardImage;
	    mnLanguage.setVisible(false);
	    break;
	case "mode":
	    currentCard = cardMode;
	    mnLanguage.setVisible(false);
	    break;
	case "automode":
	    currentCard = cardAutoMode;
	    mnLanguage.setVisible(false);
	    break;
	case "manual":
	    currentCard = cardManual;
	    cardManual.reset();
	    cardManual.setLanguage(this.languages.get(0));
	    mnLanguage.setVisible(false);
	    break;
	case "auto":
	    currentCard = cardAuto;
	    // Start running automatic translation task
	    mnLanguage.setVisible(false);
	    cardAuto.run();
	    break;
	case "end":
	    currentCard = cardEnd;
	    // Set name to be shown on screen
	    cardEnd.setSavedFileName(translator.getSavedDirectory());
	    mnLanguage.setVisible(false);
	    break;
	default:
	    showErrorMessage("Card not available.");
	}

	currentCard.setVisible(true);
    }

    /**
     * Shows an error message on the screen explaining the malfunction.
     * 
     * @param message: explanation of the error
     */
    public void showErrorMessage(String message) {
	JOptionPane.showMessageDialog(this, message, "FileLingual",
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
		"FileLingual", JOptionPane.ERROR_MESSAGE);
    }

    /*
     * ######################## CARDS #################################
     */

    private JPanel getCardMain() throws ResourceException {
	if (cardMain == null) {
	    cardMain = new CardMain(this);
	    cardMain.setVisible(true);
	}
	return cardMain;
    }

    private JPanel getCardInfo() throws ResourceException {
	if (cardInfo == null) {
	    cardInfo = new CardInfo(this);
	    cardInfo.setVisible(false);
	}
	return cardInfo;
    }

    private JPanel getCardFile() throws ResourceException {
	if (cardFile == null) {
	    cardFile = new CardFile(this);
	    cardFile.setVisible(false);
	}
	return cardFile;
    }

    private JPanel getCardImage() throws ResourceException {
	if (cardImage == null) {
	    cardImage = new CardImage(this);
	    cardImage.setVisible(false);
	}
	return cardImage;
    }

    private JPanel getCardMode() throws ResourceException {
	if (cardMode == null) {
	    cardMode = new CardMode(this);
	    cardMode.setVisible(false);
	}
	return cardMode;
    }

    private JPanel getCardAuto() throws ResourceException {
	if (cardAuto == null) {
	    cardAuto = new CardAuto(this);
	    cardAuto.setVisible(false);
	}
	return cardAuto;
    }

    private JPanel getCardAutoMode() throws ResourceException {
	if (cardAutoMode == null) {
	    cardAutoMode = new CardAutoMode(this);
	    cardAutoMode.setVisible(false);
	}
	return cardAutoMode;
    }

    private JPanel getCardManual() throws ResourceException {
	if (cardManual == null) {
	    cardManual = new CardManual(this);
	    cardManual.setVisible(false);
	}
	return cardManual;
    }

    private JPanel getCardEnd() throws ResourceException {
	if (cardEnd == null) {
	    cardEnd = new CardEnd(this);
	    cardEnd.setVisible(false);
	}
	return cardEnd;
    }

    /*
     * ######################## LOCALIZATION #################################
     */

    private JMenu getMnLanguage() {
	if (mnLanguage == null) {
	    menuBar = new JMenuBar();

	    String msg = this.messages.getString("menu.language");
	    mnLanguage = new JMenu(msg);
	    menuBar.add(mnLanguage);
	    mnLanguage.setMnemonic(msg.charAt(0));
	    mnLanguage.setDisplayedMnemonicIndex(0);
	    setJMenuBar(menuBar);
	    addMenuItems();
	}
	return mnLanguage;
    }

    private void addMenuItems() {
	String[] items = ResourceLoader.getLanguageNames(messages);
	List<JMenuItem> menuItems = new ArrayList<>();

	for (int i = 0; i < items.length; i++) {
	    String code = items[i].split(" ")[1];
	    menuItems.add(new NumberedJMenuItem(this, messages, items[i],
		    code.substring(1, code.length())));
	    mnLanguage.add(menuItems.get(i));
	}
    }

    public void localize(Locale locale) {
	this.messages = ResourceBundle.getBundle("Messages", locale);
	this.translator = new Translator(this.messages);
    }

    public ResourceBundle getMessages() {
	return this.messages;
    }

    /*
     * ######################## IMAGES #################################
     */

    /**
     * Saves selected images paths for automatic description functionality.
     * 
     * @param selectedImages file array containing info of all images
     */
    public void setImages(File[] selectedImages) {
	vision.setImages(selectedImages);
    }

    /*
     * ######################## TRANSLATION #################################
     */

    /**
     * Saves all translated files to their respective destinations.
     * 
     * @ in case of issue saving to file
     */
    public void saveAll() {
	translator.saveAll();
    }

    /**
     * Carries out automatic image captioning.
     */
    public void describe() {

	try {
	    Properties captions = vision.captions();
	    if (captions != null) {
		translator.include(vision.captions());
	    }
	} catch (ImageException ie) {

	}
    }

    /**
     * Carries out all translations (+ image description).
     * 
     * @ in case of issues with - translation api - vision api - writing to/from
     * file
     */
    public void translate() {
	describe();

	translator.translateAll();
	if (manualMode) { // Manual translation
	    translator.saveAll();
	    IDE.open(contentPane, translator.getManualPath());
	}
    }

    /**
     * Establishes the languages the user has selected for
     * 
     * @param languages list of strings, with the format "English, United
     *                  States" @
     */
    public void setLanguageAndMode(List<String> languages, boolean isManual) {
	this.languages = languages;
	if (!isManual) {
	    // Automatic translation
	    cardAutoMode.setTargetLanguages(languages);
	} else {
	    // Manual translation
	    translator.setTargetLanguages(languages, null);
	}
    }

    /**
     * Establishes the languages to which the translator will carry out the
     * translations, while also expressing which one will be the default (if
     * any).
     * 
     * @param languages   list of languages the translator will translate to
     * @param defaultLang name of the target language that will be the default -
     *                    if null, no target language is established
     */
    public void setLanguages(List<String> languages, String defaultLang) {
	translator.setTargetLanguages(languages, defaultLang);
    }

    /**
     * @return true if the translator has a directory, false otherwise
     */
    public boolean hasDirectory() {
	return translator.getSavedDirectory() != null;
    }

    /**
     * Inputs file defined in a given source path, to be processed and checked
     * for mistakes.
     * 
     * @ in case the file is not correct
     */
    public void inputFile() {
	translator.input();
    }

    /**
     * @param filePath of the source file that will be processed
     */
    public void from(String filePath) {
	translator.from(filePath);
    }

    /**
     * @param dirPath of directory where all files will be saved @
     */
    public void to(String dirPath) {
	translator.to(dirPath);
    }

    /**
     * Resets everything to their initial, blank state; so that translation
     * process can be carriedo ut again.
     * 
     * @ in case of issues resetting
     */
    public void reset() {
	translator.reset();
	cardMode.reset();
	manualMode = false;
    }

    /**
     * Reviews all automatically-translated files (opens them in
     * system-dependent IDE).
     * 
     * @
     */
    public void review() {
	for (String path : translator.review()) {
	    IDE.open(contentPane, path);
	}
    }

    /**
     * Sets translation mode (either manual or automatic).
     * 
     * @param path @
     */
    public void setMode(String path) {
	translator.to(path);
	if (path == null) {
	    translator.setAutoMode();
	    manualMode = false;
	} else {
	    translator.setManualMode();
	    manualMode = true;
	}
    }

}
