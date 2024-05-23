package main.java.gui.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JPanel;

import main.java.util.exception.IdeException;
import main.java.util.exception.ResourceException;

/**
 * Manages the opening of a text editing tool/Integrated Development Environment
 * in the user's native Operating System, with the aim of editing one or several
 * files found in specific file paths.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
public class IDE {

    /**
     * Opens and IDE for editing several files, in bulk.
     * 
     * @param frame panel relative to which the IDE will be opened
     * @param paths list of the files to edit
     * 
     * @throws IOException       if text editing tool cannot be opened
     * @throws ResourceException if one of the requested file paths does not
     *                           exist
     */
    public static void open(JPanel frame, List<String> paths)
	    throws ResourceException, IOException {
	for (String path : paths) {
	    open(frame, path);
	}
    }

    /**
     * Opens and IDE for editing a file.
     * 
     * @param frame panel relative to which the IDE will be opened
     * @param path  of the file to edit
     * 
     * @throws IdeException      in case of issues opening text editing tool
     *                           desktop
     * @throws ResourceException in case that requested file path does not exist
     */
    public static void open(JPanel frame, String path)
	    throws IdeException, ResourceException {
	File file = new File(path);

	if (file.exists()) {
	    try {
		if (!code(file)) {
		    text(file);
		}
	    } catch (IOException e) {
		throw new IdeException();
	    }
	} else {
	    throw new ResourceException(path);
	}
    }

    /**
     * (Tries to) Opens a file with Visual Studio Code.
     * 
     * @param file the file to open
     * @return true if the file is successfully opened with VS Code, false
     *         otherwise
     * @throws IOException if not supported by the user's system
     */
    private static boolean code(File file) throws IOException {
	try {
	    // Try to open the file with Visual Studio Code
	    String[] command = { "code", file.getAbsolutePath() };
	    // Execute the command
	    ProcessBuilder processBuilder = new ProcessBuilder(command);
	    processBuilder.start();
	    return true;
	} catch (Exception e) {
	    // Restore interrupted state and return false
	    Thread.currentThread().interrupt();
	    return false;
	}
    }

    /**
     * Opens a file with a generic text editor, platform-dependent.
     * 
     * @param file the file to open
     * @throws IOException if editing files from desktop is not supported by the
     *                     user's system
     */
    private static void text(File file) throws IOException {
	// Use platform-specific method to open the file with default text
	// editor
	String osName = System.getProperty("os.name").toLowerCase();
	if (osName.contains("win")) {
	    Runtime.getRuntime().exec("notepad.exe " + file.getAbsolutePath());
	} else if (osName.contains("mac")) {
	    Runtime.getRuntime().exec("open -e " + file.getAbsolutePath());
	} else if (osName.contains("nix") || osName.contains("nux")
		|| osName.contains("aix")) {
	    Runtime.getRuntime().exec("xdg-open " + file.getAbsolutePath());
	} else {
	    // For other platforms, you might handle this case differently
	    throw new UnsupportedOperationException(
		    "Opening files with default text editor is not supported on this platform.");
	}
    }
}
