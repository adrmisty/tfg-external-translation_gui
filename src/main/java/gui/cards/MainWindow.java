package main.java.gui.cards;

import java.awt.CardLayout;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import main.java.gui.util.ExceptionHandler;
import main.java.gui.util.IDE;
import main.java.gui.util.NumberedJMenuItem;
import main.java.logic.file.SourceFile;
import main.java.logic.file.TargetFile;
import main.java.logic.image.Vision;
import main.java.logic.speech.Speech;
import main.java.logic.translation.Translator;
import main.java.util.exception.ImageException;
import main.java.util.exception.LanguageException;
import main.java.util.exception.PropertiesException;
import main.java.util.exception.ResourceException;
import main.java.util.exception.ResultsException;
import main.java.util.exception.SpeechException;
import main.java.util.exception.TranslationException;
import main.java.util.exception.UIException;
import main.java.util.properties.ResourceLoader;

/**
 * Main Window of the application, which consists of several different views
 * organised and displayed with a Card Layout.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version 2.0 (February 2024)
 */
public class MainWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    /*
     * ##############################
     */
    private Translator translator;
    private Vision vision;
    private Speech speech;

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
    private CardTextToSpeech cardTTS; // For cognitive speech
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
     * @throws ResourceException
     * 
     * @
     */
    public MainWindow() throws ResourceException {
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
     * @throws ResourceException
     */
    public void initWindow(Locale locale) throws ResourceException {
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

	// Localization
	localize(locale);

	// Image description
	this.vision = new Vision();
	this.speech = new Speech(messages);

	// Card container
	contentPane.add(getCardMain());
	contentPane.add(getCardInfo());
	contentPane.add(getCardFile());
	contentPane.add(getCardImage());
	contentPane.add(getCardMode());
	contentPane.add(getCardAutoMode());
	contentPane.add(getCardAuto());
	contentPane.add(getCardTTS());
	contentPane.add(getCardManual());
	contentPane.add(getCardEnd());
	getMnLanguage();

	cardMode.reset();
	currentCard = cardMain;
	validate();
	repaint();
    }

    /**
     * Shows a new card in the main window.
     * 
     * @param String identificating the new card to show @ if the card to show
     *               is not recognised
     */
    public void show(String newCard) {
	currentCard.setVisible(false);
	mnLanguage.setVisible(false);

	switch (newCard) {
	case "main":
	    currentCard = cardMain;
	    mnLanguage.setEnabled(true);
	    mnLanguage.setVisible(true);
	    break;
	case "file":
	    currentCard = cardFile;
	    break;
	case "info":
	    currentCard = cardInfo;
	    break;
	case "image":
	    currentCard = cardImage;
	    break;
	case "mode":
	    currentCard = cardMode;
	    break;
	case "automode":
	    currentCard = cardAutoMode;
	    break;
	case "manual":
	    cardManual.reset();
	    cardManual.setLanguage(this.languages.get(0));
	    currentCard = cardManual;
	    break;
	case "tts":
	    cardTTS.reset();
	    cardTTS.setTargetFiles(translator.getResults());
	    currentCard = cardTTS;
	    break;
	case "auto":
	    currentCard = cardAuto;
	    // Start running automatic translation task
	    cardAuto.run();
	    break;
	case "auto-2":
	    // Already run
	    currentCard = cardAuto;
	    break;
	case "end":
	    // Set name to be shown on screen
	    cardEnd.setSavedFileName(translator.getSavedDirectory());
	    currentCard = cardEnd;
	    break;
	default:
	    // Unrecoverable
	    ExceptionHandler.handle(this, new UIException(messages), true);
	}

	currentCard.setVisible(true);
    }

    /**
     * Handles the caught exception, depending on its type and the program's
     * locale, as well as whether the application should be exited or not.
     * 
     * @param exception object representing caught exception
     * @param exit      boolean true if applicatin should be exited, or not
     */
    public void showErrorMessage(Exception e, boolean exit) {
	ExceptionHandler.handle(this, e, exit);
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

    private JPanel getCardTTS() throws ResourceException {
	if (cardTTS == null) {
	    cardTTS = new CardTextToSpeech(this);
	    cardTTS.setVisible(false);
	}
	return cardTTS;
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
		    code.substring(1, code.length() - 1)));
	    mnLanguage.add(menuItems.get(i));
	}
    }

    public void localize(Locale locale) {
	ResourceBundle retrieved = ResourceBundle.getBundle("Messages", locale);

	// Not available locale!
	if (!(locale.equals(Locale.getDefault()))
		&& (retrieved.getLocale().equals(Locale.getDefault()))) {
	    this.showErrorMessage(new LanguageException(messages, locale),
		    false);
	} else if (retrieved == null) {
	    this.showErrorMessage(new ResourceException(messages,
		    "Messages" + locale.toLanguageTag()), true);

	}

	this.messages = retrieved;
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
     */
    public void saveAll() {

	try {
	    translator.saveAll();
	} catch (PropertiesException pe) {
	    this.showErrorMessage(new PropertiesException(messages,
		    pe.getFilename(), pe.isContentRelated()), true);
	} catch (IOException io) {
	    this.showErrorMessage(io, false);
	}
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
	    // Pass that exception to localized handler
	    this.showErrorMessage(
		    new ImageException(messages, ie.getInvalidFiles()), false);
	}
    }

    /**
     * Carries out all translations (+ image description).
     * 
     * @throws IOException
     * @throws PropertiesException
     * @throws TranslationException
     */
    public void translate() {
	describe();

	try {
	    translator.translateAll();
	    if (manualMode) { // Manual translation
		translator.saveAll();
		IDE.open(contentPane, translator.getManualPath());
	    }
	} catch (PropertiesException pe) {
	    this.showErrorMessage(new PropertiesException(messages,
		    pe.getFilename(), pe.isContentRelated()), true);
	} catch (IOException io) {
	    this.showErrorMessage(io, false);
	} catch (TranslationException te) {
	    this.showErrorMessage(
		    new TranslationException(messages, te.isManual()), false);
	}
    }

    /**
     * Establishes the languages the user has selected for
     * 
     * @param languages list of strings, with the format "English, United
     *                  States" @
     */
    public void setLanguageAndMode(List<String> languages, boolean isManual) {
	try {
	    if (!isManual) {
		// Automatic translation
		cardAutoMode.setTargetLanguages(languages);
	    } else {
		// Manual translation
		translator.setTargetLanguages(languages, null);
	    }
	} catch (ResourceException re) {
	    // Unrecoverable
	    this.showErrorMessage(
		    new ResourceException(messages, re.getResourceName()),
		    true);
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
     * @return boolean true if file is accepted by the translator
     */
    public boolean inputFile() {
	try {
	    translator.input();
	    return true;
	} catch (PropertiesException pe) {
	    this.showErrorMessage(new PropertiesException(messages,
		    pe.getFilename(), pe.isContentRelated()), false);
	} catch (IOException io) {
	    this.showErrorMessage(io, false);
	}
	return false;
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
     * process can be carried out again.
     */
    public void reset() {
	translator.reset();
	manualMode = false;
	initWindow(Locale.getDefault());
	show("main");
    }

    /**
     * Reviews all automatically-translated files (opens them in
     * system-dependent IDE).
     */
    public void review() {
	try {
	    for (String path : translator.review()) {
		IDE.open(contentPane, path);
	    }
	} catch (IOException io) {
	    this.showErrorMessage(new ResultsException(messages, true), false);
	}
    }

    /**
     * Reads the source file that has been input to the system.
     * 
     * @param read whether to read or to stop reading
     * @return boolean true if executed normally, false if an exception arises
     */
    public boolean readInputFile(boolean read) {
	if (!read) {
	    speech.stop();
	}
	SourceFile f = translator.getSource();

	// To read or to stop
	// Automatically stops if an exception arises
	if (read) {
	    try {
		speech.speak(f.getLanguage(), f.getContent());
	    } catch (SpeechException e) {
		// this.showErrorMessage(new SpeechException(messages), false);
		return false;
	    }
	}
	return true;
    }

    /**
     * Reads the specified target file.
     * 
     * @param read whether to read or to stop reading
     * @return boolean true if executed normally, false if an exception arises
     */
    public boolean readTargetFile(TargetFile f, boolean read) {
	if (!read) {
	    speech.stop();
	}

	// To read or to stop
	// Automatically stops if an exception arises
	if (read) {
	    try {
		speech.speak(f.getTargetCode(), f.getContent());
	    } catch (SpeechException e) {
		// this.showErrorMessage(new SpeechException(messages), false);
		return false;
	    }
	}
	return true;
    }

    /**
     * @param language, format "code-country" - if null, it refers to the source
     *                  file
     * @return boolean true if the API is currently available for speech in this
     *         language
     */
    public boolean isSpeechAvailableFor(String language) {
	if (language == null) {
	    return speech.isAvailableFor(translator.getSource().getLanguage());
	}
	return speech.isAvailableFor(language);
    }

    /**
     * Sets translation mode (either manual or automatic).
     * 
     * @param path @
     * @throws ResourceException
     */
    public void setMode(String path) throws ResourceException {
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
