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

public class NumberedJMenuItem extends JMenuItem {
    private static final long serialVersionUID = 1L;
    private String language_code;
    private Locale previous_language;

    public NumberedJMenuItem(MainWindow root, ResourceBundle messages,
	    String text, String language) {
	super(text);

	this.language_code = language;
	this.setMnemonic(this.language_code.charAt(0));
	this.setDisplayedMnemonicIndex(0);
	setForeground(UIManager.getColor("CheckBox.darkShadow"));

	this.previous_language = Locale.getDefault();

	this.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		try {
		    // Save the previous locale for possible mistakes in init
		    // process
		    previous_language = Locale.getDefault();
		    root.initWindow(new Locale(language_code));
		} catch (ResourceException re) {
		    // Handle exception:
		    // Localization into this language is not available atm
		    // Show error message + go on with application, no drama
		    root.showErrorMessage(
			    new LanguageException(messages, language), false);
		} catch (MissingResourceException mre) {
		    // Handle exception:
		    // Localization into this language is not available atm
		    // Show error message + go on with application, no drama
		    root.showErrorMessage(
			    new LanguageException(messages, language), false);
		    // Go back to previous language
		    root.initWindow(previous_language);
		}

	    }
	});

    }
}
