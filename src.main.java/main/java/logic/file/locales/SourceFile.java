package main.java.logic.file.locales;

import java.io.IOException;

import main.java.logic.file.FileManager;
import main.java.logic.file.LocaleFile;
import main.java.util.PropertyLoader;
import main.java.util.ResourceLoader;
import main.java.util.exception.PropertiesException;
import main.java.util.other.FileUtil;

/**
 * Source localization file, which is saved in the user's storage system, and
 * will be translated into one or more target files.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
public class SourceFile extends LocaleFile {

    public SourceFile(FileManager manager, String path)
	    throws PropertiesException, IOException {
	super(manager);
	process(path);
    }

    /**
     * Processes the file found in a given path, and checks whether a file is
     * compliant with the i18n format for localization. In case it is, it
     * processes all information found in the file (bundle name, language code
     * and contents).
     * 
     * @param filepath path to the .properties file
     * @throws PropertiesException in case that input file is not a valid i18n
     *                             properties file
     * @throws IOException         in case of issues reading the input filepath
     */
    public void process(String filepath)
	    throws PropertiesException, IOException {

	// Not a .properties file
	if (!ResourceLoader.getFileExtension(filepath).get()
		.equals("properties")) {
	    throw new PropertiesException(filepath, false);
	}

	// Validate both path and contents
	setPath(filepath);
	setContent(PropertyLoader.load(filepath, false));

	// Process file name and language
	String[] name = FileUtil.unformat(filepath);
	setBundleName(name[0]);
	setCode(name[1]);
    }

}
