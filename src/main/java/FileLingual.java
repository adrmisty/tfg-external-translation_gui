package main.java;

import java.awt.EventQueue;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import main.java.gui.cards.MainWindow;
import main.java.util.properties.ResourceLoader;

public class FileLingual {

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		try {
		    config();
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

    /**
     * Look and feel configurations for the app. Localization configuration.
     * 
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws UnsupportedLookAndFeelException
     */
    private static void config()
	    throws ClassNotFoundException, InstantiationException,
	    IllegalAccessException, UnsupportedLookAndFeelException {
	// Locale.setDefault(new Locale("en")); for testing
	UIManager.setLookAndFeel("com.formdev.flatlaf.themes.FlatMacLightLaf");
	setFont();
    }

    private static void setFont() {
	Font defaultFont = ResourceLoader.getFont().deriveFont(13f);
	UIManager.put("MenuBar.font", defaultFont);
	UIManager.put("MenuItem.font", defaultFont);
	UIManager.put("RadioButtonMenuItem.font", defaultFont);
	UIManager.put("CheckBoxMenuItem.font", defaultFont);
	UIManager.put("OptionPane.messageFont", defaultFont);
	UIManager.put("OptionPane.buttonFont", defaultFont);
	UIManager.put("ToolTip.font", defaultFont);
	UIManager.put("Button.font", defaultFont);
	UIManager.put("Label.font", defaultFont);
	UIManager.put("TitledBorder.font", defaultFont);
	UIManager.put("InternalFrame.titleFont", defaultFont); // Set font for
							       // the window
							       // title
    }
}
