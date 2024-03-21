package main.java.gui.other;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JMenuItem;

import main.java.gui.MainWindow;

public class NumberedJMenuItem extends JMenuItem {
    private static final long serialVersionUID = 1L;
    private String language_code;

    public NumberedJMenuItem(MainWindow root, ResourceBundle messages,
	    String text, String language) {
	super(text);
	this.language_code = language;

	this.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		try {
		    root.initWindow(new Locale(language_code));
		} catch (Exception e1) {
		    root.showErrorMessage(
			    messages.getString("error.localization"));
		}
	    }
	});

    }
}
