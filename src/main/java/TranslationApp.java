package main.java;

import java.awt.EventQueue;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;

import main.java.gui.cards.MainWindow;
import main.java.logic.util.properties.ResourceLoader;

public class TranslationApp {

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		try {
		    Locale.setDefault(new Locale("en_US"));
		    UIManager.setLookAndFeel(
			    "com.formdev.flatlaf.themes.FlatMacLightLaf");
		    MainWindow frame = new MainWindow();
		    frame.setVisible(true);
		} catch (Exception e1) {
		    // Log error
		    Logger.getLogger(ResourceLoader.class.getName())
			    .log(Level.SEVERE, null, e1);
		    System.exit(0);
		}
	    }
	});
    }

}
