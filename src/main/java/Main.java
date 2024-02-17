package main.java;

import java.awt.EventQueue;
import java.util.Locale;

import javax.swing.UIManager;

import main.java.gui.MainWindow;

public class Main {

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		try {
		    Locale.setDefault(Locale.US);
		    UIManager.setLookAndFeel(
			    "com.formdev.flatlaf.themes.FlatMacLightLaf");
		    MainWindow frame = new MainWindow();
		    frame.setVisible(true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

}
