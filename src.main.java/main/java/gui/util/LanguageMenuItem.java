package main.java.gui.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JMenuItem;
import javax.swing.UIManager;

import main.java.gui.cards.MainWindow;
import main.java.util.exception.LanguageException;
import main.java.util.exception.ResourceException;

/**
 * Represents a numbered menu item for language selection in a JMenu. Each menu
 * item has its own associated language which, when clicked on, localizes the
 * main window it is relative to.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public class LanguageMenuItem extends JMenuItem {

    private static final long serialVersionUID = 1L;

    private String language;
    private Locale defaultLanguage;

    public LanguageMenuItem(MainWindow root, ResourceBundle messages,
	    String text, String language) {
	super(text);

	this.language = language;
	this.defaultLanguage = Locale.getDefault();

	// The button's mnemonic will be the language's first letter
	this.setMnemonic(this.language.charAt(0));
	if (this.language.equals("en")) {
	    this.setMnemonic(this.language.charAt(1));
	}

	setForeground(UIManager.getColor("CheckBox.darkShadow"));

	this.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		try {
		    // Previous locale, currently the default one
		    defaultLanguage = Locale.getDefault();
		    root.initWindow(new Locale(language));
		} catch (ResourceException re) {
		    root.showErrorMessage(
			    new LanguageException(messages, language), false);
		} catch (MissingResourceException mre) {
		    root.showErrorMessage(
			    new LanguageException(messages, language), false);
		    // Undo any localization
		    root.initWindow(defaultLanguage);
		}

	    }
	});

    }
}
