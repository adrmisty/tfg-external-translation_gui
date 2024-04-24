package main.java.gui.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JMenuItem;

import main.java.gui.cards.MainWindow;
import main.java.util.exception.LanguageException;
import main.java.util.exception.ResourceException;

public class NumberedJMenuItem extends JMenuItem {
    private static final long serialVersionUID = 1L;
    private String language_code;

    public NumberedJMenuItem(MainWindow root, ResourceBundle messages,
	    String text, String language) {
	super(text);
	this.language_code = language;
	this.setMnemonic(this.language_code.charAt(0));
	this.setDisplayedMnemonicIndex(0);

	this.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		try {
		    root.initWindow(new Locale(language_code));
		} catch (ResourceException re) {
		    // Handle exception:
		    // Localization into this language is not available atm
		    // Show error message + go on with application, no drama
		    root.showErrorMessage(
			    new LanguageException(messages, language), false);
		}
	    }
	});

    }
}
