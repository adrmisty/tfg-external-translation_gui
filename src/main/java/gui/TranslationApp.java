package main.java.gui;

import java.awt.EventQueue;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;

import main.java.gui.cards.MainWindow;
import main.java.util.ResourceLoader;

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
		} catch (Exception e) {
		    // Log error
		    // Respective visual error in MainWindow
		    Logger.getLogger(ResourceLoader.class.getName())
			    .log(Level.SEVERE, null, e);
		}
	    }
	});
    }

}
