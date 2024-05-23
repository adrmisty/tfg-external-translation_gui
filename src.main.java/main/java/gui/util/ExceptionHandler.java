package main.java.gui.util;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Handles exceptions that arise from the program's logic, due to: - execution
 * interruptions - api time outs and other issues - not found resources -
 * incorrect format of provided resources - etc.
 * 
 * A specific and context-dependent error message in the application's specified
 * locale is shown to the user, and measures are taken to either exit the
 * application or not, based on the exception's severity.
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

	// Log all exceptions
	// Logger.getLogger(ExceptionHandler.class.getName()).log(Level.SEVERE,
	// null, e);

	JOptionPane.showMessageDialog(root, e.getLocalizedMessage(),
		"FileLingual", JOptionPane.ERROR_MESSAGE);
	if (exit) {
	    System.exit(0);
	}
    }

}
