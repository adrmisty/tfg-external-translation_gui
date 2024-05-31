package main.java.gui.cards;

import java.awt.CardLayout;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import main.java.gui.cards.app.CardAuto;
import main.java.gui.cards.app.CardDefault;
import main.java.gui.cards.app.CardEnd;
import main.java.gui.cards.app.CardFile;
import main.java.gui.cards.app.CardImage;
import main.java.gui.cards.app.CardInfo;
import main.java.gui.cards.app.CardMain;
import main.java.gui.cards.app.CardManual;
import main.java.gui.cards.app.CardMode;
import main.java.gui.cards.app.CardTextToSpeech;
import main.java.gui.util.ExceptionHandler;
import main.java.gui.util.IDE;
import main.java.gui.util.LanguageMenuItem;
import main.java.logic.file.locales.LocaleFile;
import main.java.logic.image.Vision;
import main.java.logic.speech.Speech;
import main.java.logic.translation.TranslationManager;
import main.java.util.exception.IdeException;
import main.java.util.exception.ImageException;
import main.java.util.exception.IncompleteResultsException;
import main.java.util.exception.LanguageException;
import main.java.util.exception.PropertiesException;
import main.java.util.exception.ResourceException;
import main.java.util.exception.ResultsException;
import main.java.util.exception.TranslationException;
import main.java.util.exception.UIException;
import main.java.util.resources.ResourceLoader;

/**
 * Main Window of the application.
 * 
 * In terms of GUI, it consists of several different cards (and their respective
 * help cards), as well of the flow between components and actions.
 * 
 * In terms of logic, it manages all exceptions thrown in the application's
 * domain logic execution.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
public class MainWindow extends JFrame {

    private static final long serialVersionUID = 1L;

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
    private CardDefault cardDefault; // For automatic translation settings
    private CardAuto cardAuto; // For automatic translation
    private CardTextToSpeech cardTTS; // For cognitive speech
    private CardManual cardManual; // For manual translation
    private CardEnd cardEnd; // For ending the app

    // Language menu
    private JMenuBar menuBar;
    private JMenu mnLanguage;

    // Logic
    private TranslationManager translator;
    private Vision vision;
    private Speech speech;

    // Variables to show on screen
    private boolean manualMode = false;
    private boolean saved = false;
    private List<String> languages = new ArrayList<>();

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
	cardDefault = null;
	cardManual = null;
	cardEnd = null;
	cardTTS = null;
	cardImage = null;
	mnLanguage = null;
	menuBar = null;
	currentCard = null;

	// Localization
	if (locale != null) {
	    localize(locale);
	}

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

	currentCard = getCurrentCard();
	validate();
	repaint();

	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(WindowEvent e) {

		if (JOptionPane.showConfirmDialog(null,
			getMessages().getString("label.exit.app"),
			"FileLingual",
			JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
		    // Close the application
		    dispose();
		}
	    }
	});

    }

    private JPanel getCurrentCard() {
	if (currentCard == null) {
	    return cardMain;
	}
	return currentCard;
    }

    /**
     * Shows a new card in the main window.
     * 
     * @param String identificating the new card to show @ if the card to show
     *               is not recognised
     */
    public void show(String newCard) {
	currentCard.setVisible(false);
	getMnLanguage().setVisible(false);

	switch (newCard) {
	case "main":
	    currentCard = cardMain;
	    mnLanguage.setEnabled(true);
	    mnLanguage.setVisible(true);
	    break;
	case "file":
	    cardFile.setKeyEventDispatcher(true);
	    currentCard = cardFile;
	    break;
	case "info":
	    currentCard = cardInfo;
	    break;
	case "image":
	    cardImage.setKeyEventDispatcher(true);
	    currentCard = cardImage;
	    break;
	case "mode":
	    cardMode.setKeyEventDispatcher(true);
	    translator.resetLanguages();
	    currentCard = cardMode;
	    break;
	case "automode":
	    cardDefault.setKeyEventDispatcher(true);
	    currentCard = cardDefault;
	    break;
	case "manual":
	    cardManual.reset();
	    cardManual.setKeyEventDispatcher(true);
	    cardManual.setLanguages(this.languages);
	    currentCard = cardManual;
	    break;
	case "tts":
	    cardTTS.reset();
	    cardTTS.setKeyEventDispatcher(true);
	    cardTTS.setTargetFiles(translator.getTargetFiles());
	    currentCard = cardTTS;
	    break;
	case "auto":
	    currentCard = cardAuto;
	    cardDefault.setKeyEventDispatcher(true);
	    // Start running automatic translation task
	    cardAuto.run();
	    break;
	case "auto-2":
	    // Already run
	    cardAuto.setKeyEventDispatcher(true);
	    currentCard = cardAuto;
	    break;
	case "end":
	    // Set name to be shown on screen
	    cardEnd.setSavedFileName(translator.getPath(), saved);
	    currentCard = cardEnd;
	    break;
	default:
	    // Unrecoverable
	    ExceptionHandler.handle(this, new UIException(messages), true);
	}

	speech.stop();
	getCurrentCard().setVisible(true);
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
	if (cardDefault == null) {
	    cardDefault = new CardDefault(this);
	    cardDefault.setVisible(false);
	}
	return cardDefault;
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
	    menuBar.setForeground(UIManager.getColor("Button.shadow"));

	    String msg = this.messages.getString("menu.language");
	    mnLanguage = new JMenu(msg);

	    // Place language menu on the right
	    menuBar.add(Box.createHorizontalGlue());
	    menuBar.add(mnLanguage);
	    mnLanguage.setMnemonic(msg.charAt(0));
	    mnLanguage.setDisplayedMnemonicIndex(0);
	    mnLanguage.setForeground(UIManager.getColor("Button.shadow"));
	    setJMenuBar(menuBar);
	    addMenuItems();
	}
	return mnLanguage;
    }

    private void addMenuItems() {
	String[] items = ResourceLoader.getLanguageNamesForMenu(messages);
	List<JMenuItem> menuItems = new ArrayList<>();

	for (int i = 0; i < items.length; i++) {
	    String code = items[i].split(" ")[1];
	    menuItems.add(new LanguageMenuItem(this, messages, items[i],
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
	this.translator = new TranslationManager(this.messages);
    }

    public ResourceBundle getMessages() {
	return this.messages;
    }

    /*
     * ######################## IMAGES #################################
     */

    /**
     * Saves selected images paths for automatic description functionality.
     * Shows an error message if the any of the provided files are not valid for
     * description.
     * 
     * @param selectedImages file array containing info of all images
     */
    public void setImages(File[] selectedImages) {
	try {
	    vision.setImages(selectedImages);
	} catch (ImageException e) {
	    this.showErrorMessage(
		    new ImageException(messages, e.getInvalidFiles()), false);
	}
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
	    saved = true;
	} catch (ResultsException re) {
	    this.showErrorMessage(new ResultsException(messages, re.isReview(),
		    re.isFilePath(), re.getFileMove()), false);
	} catch (PropertiesException pe) {
	    this.showErrorMessage(new PropertiesException(messages,
		    pe.getFilename(), pe.isContentRelated()), true);
	} catch (IOException io) {
	    this.showErrorMessage(io, false);
	}
    }

    /**
     * Carries out automatic image captioning.
     * 
     * @throws ImageException
     */
    public void describe() throws ImageException {
	Properties captions = vision.describe();
	if (captions != null) {
	    translator.include(captions);
	}
    }

    /**
     * Executes image description of input images (if any) and translations of
     * the texts found in the source file provided by the user.
     */
    public void translate() {

	try {
	    describe();
	    translator.translateAll();
	    if (manualMode) { // Manual translation
		translator.saveAll();
		IDE.open(contentPane, translator.getResultsPaths());
		saved = true;
	    }
	} catch (PropertiesException pe) {
	    this.showErrorMessage(new PropertiesException(messages,
		    pe.getFilename(), pe.isContentRelated()), true);
	} catch (ImageException img) {
	    this.showErrorMessage(
		    new ImageException(messages, img.getInvalidFiles()), false);
	} catch (IOException ide) {
	    this.showErrorMessage(new IdeException(messages), false);
	} catch (TranslationException te) {
	    this.showErrorMessage(
		    new TranslationException(messages, te.isManual()), false);

	    // Any other type of exception during translator process
	    // Go back to mode
	} catch (Exception e) {
	    this.showErrorMessage(e, false);
	    cardAuto.reset();
	    cardManual.reset();
	    // Go back
	    show("mode");
	}
    }

    /**
     * Establishes the languages the user has selected for a given translation
     * mode.
     * 
     * @param languages list of strings, with the format "English, United
     *                  States" or "English", in the app's localized language
     */
    public void setTranslationMode(List<String> languages, boolean isManual) {
	try {
	    this.languages = languages;
	    if (!isManual) {
		// Automatic translation
		cardDefault.setTargetLanguages(languages);
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
    public void setLanguagesForAutomatic(List<String> languages,
	    String defaultLang) {
	translator.setTargetLanguages(languages, defaultLang);
    }

    /**
     * @param filePath of the source file that will be processed
     */
    public boolean inputFile(String path) {
	try {
	    translator.input(path);

	    // If everything goes well
	    cardDefault.setDefaultSource(translator.getSource().isDefault());
	    cardAuto.setSourcePath(path);
	    cardManual.setSourcePath(path);
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
	vision.reset();
	manualMode = false;
	initWindow(null);
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
	    this.showErrorMessage(new IdeException(messages, true), false);
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
	LocaleFile f = translator.getSource();

	// To read or to stop
	// Automatically stops if an exception arises
	if (read) {
	    speech.speak(f.getCode(), f.getContent());
	}
	return true;
    }

    /**
     * Reads the specified target file.
     * 
     * @param read whether to read or to stop reading
     * @return boolean true if executed normally, false if an exception arises
     */
    public boolean readTargetFile(LocaleFile currentFile, boolean read) {
	if (!read) {
	    speech.stop();
	}

	// To read or to stop
	// Automatically stops if an exception arises
	if (read) {
	    speech.speak(currentFile.getCode(), currentFile.getContent());
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
	    String code = translator.getSource().getCode();
	    if (code != null) {
		return speech.isAvailableFor(translator.getSource().getCode());
	    }
	    return false;
	}
	return speech.isAvailableFor(language);
    }

    /**
     * @return boolean true if all sentences found in source file have been
     *         completely translated, false otherwise
     */
    public boolean areResultsComplete() {
	try {
	    translator.areResultsComplete();
	} catch (IncompleteResultsException e) {
	    this.showErrorMessage(new IncompleteResultsException(messages),
		    false);
	}

	return true;
    }

    /**
     * Sets translation mode (either manual or automatic).
     * 
     * @param path @
     * @throws ResourceException
     */
    public void setMode(String path) {
	translator.to(path);
	try {
	    if (path == null) {
		translator.setAutoMode();
		manualMode = false;
	    } else {
		translator.setManualMode();
		manualMode = true;
	    }
	} catch (ResourceException e) {
	    this.showErrorMessage(e, true);
	} catch (SQLException se) {
	    this.showErrorMessage(se, false);
	}
    }

}
