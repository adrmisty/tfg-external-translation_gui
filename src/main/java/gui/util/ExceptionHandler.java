package main.java.gui.util;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Handles exceptions that arise from the program's logic, due to: - execution
 * interruptions - api time outs and other issues - not found resources -
 * incorrect format of provided resources - etc.
 * 
 * This exception is handling is done through the GUI's point of view, that is,
 * providing a given error message in the application's specified locale.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public class ExceptionHandler {

    /**
     * Shows an error dialog for the caught exception's localized message.
     * 
     * @param root frame the dialog message will be shown relative to
     * @param e    caught exception
     * @param exit boolean true whether application should be exited or not
     */
    public static void handle(JFrame root, Exception e, boolean exit) {

	JOptionPane.showMessageDialog(root, e.getLocalizedMessage(),
		"FileLingual", JOptionPane.ERROR_MESSAGE);
	if (exit) {
	    System.exit(0);
	}
    }

}
