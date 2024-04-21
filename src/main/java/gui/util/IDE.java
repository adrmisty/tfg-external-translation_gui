package main.java.gui.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class IDE {

    /**
     * Opens and IDE for editing a file given by a path, in a specific operating
     * system.
     * 
     * @param panel relative to which the IDE will be opened
     * @param path  of the file to edit
     */
    public static void open(JPanel frame, String path) {
	File file = new File(path);

	if (Desktop.isDesktopSupported()) {
	    if (file.exists()) {
		try {
		    String os = System.getProperty("os.name").toLowerCase();

		    // Windows
		    if (os.contains("win")) {
			Runtime.getRuntime().exec(new String[] { "cmd", "/c",
				"code", file.getAbsolutePath() });

			// Unix/Linux/MacOS
			// TextEdit by the default, can specify Notepad++ or
			// others
		    } else if (os.contains("nix") || os.contains("nux")
			    || os.contains("mac")) {
			Runtime.getRuntime().exec(new String[] { "open", "-a",
				"TextEdit", file.getAbsolutePath() });
		    } else {
			JOptionPane.showMessageDialog(frame,
				"Oops! Your operating system is not yet supported by the application :(",
				"Error: unsupported OS",
				JOptionPane.ERROR_MESSAGE);
		    }
		} catch (IOException ex) {
		    ex.printStackTrace();
		    JOptionPane.showMessageDialog(frame,
			    "Oops! There has been an error when opening the editor :(",
			    "Error: cannot open editor",
			    JOptionPane.ERROR_MESSAGE);
		}
	    } else {
		JOptionPane.showMessageDialog(frame,
			"Oops! We could not find the file you wanted to review :(",
			"Error", JOptionPane.ERROR_MESSAGE);
	    }
	} else {
	    JOptionPane.showMessageDialog(frame,
		    "Oops! Desktop is not supported :(", "Error",
		    JOptionPane.ERROR_MESSAGE);
	}
    }

    /**
     * Opens and IDE for editing several files.
     * 
     * @param panel relative to which the IDE will be opened
     * @param paths list of the files to edit
     */
    public static void open(JPanel frame, List<String> paths) {
	for (String path : paths) {
	    open(frame, path);
	}
    }

}
