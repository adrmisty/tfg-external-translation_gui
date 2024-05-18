package main.java.gui.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JPanel;

import main.java.util.exception.ResourceException;

/**
 * Class for managing the opening of a text editing tool/Integrated Development
 * Environment in the user's native Operating System, with the aim of editing
 * one or several files found in specific file paths.
 * 
 * @author Adriana R.F. - uo282798@uniovi.es
 * @version May 2024
 */
public class IDE {

    /**
     * Opens and IDE for editing several files, in bulk.
     * 
     * @param panel relative to which the IDE will be opened
     * @param paths list of the files to edit
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
     * @param panel relative to which the IDE will be opened
     * @param path  of the file to edit
     * @throws IOException       in case of issues opening text editing tool
     *                           desktop
     * @throws ResourceException in case that requested file path does not exist
     */
    public static void open(JPanel frame, String path)
	    throws IOException, ResourceException {
	File file = new File(path);

	if (file.exists()) {
	    Desktop.getDesktop().edit(file);
	} else {
	    throw new ResourceException(path);
	}
    }

}
