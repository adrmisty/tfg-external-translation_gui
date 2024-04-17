package main.java.logic.translation.mode;

import java.util.Properties;

import main.java.logic.translation.TranslationMode;
import main.java.logic.util.file.LocaleFileWriter;

/**
 * Manual translation mode (translator user interacts directly with file and
 * writes its desired translations on it).
 * 
 * 
 * @author Adriana R.F. (uo282798)
 * @version March 2024
 */
public class ManualTranslation implements TranslationMode {

    private String path;
    private LocaleFileWriter file;

    public ManualTranslation(LocaleFileWriter file, String path) {
	this.file = file;
	this.path = path;
    }

    /**
     * Writes property keys into a new file so that the user can execute the
     * manual translation themselves.
     * 
     * @throws Exception in case of issue with I/O writing to file
     */
    @Override
    public Properties translate(String language) throws Exception {
	write();
	return null; // Nothing to save!
    }

    /**
     * Writes automatic translation results to target file.
     * 
     * @throws Exception in case of writing issues
     */
    @Override
    public void write() throws Exception {
	file.manualWrite(path);
    }

    /**
     * Resets the cache and eliminates all of its records.
     */
    @Override
    public void reset() {
	// Nothing to do!
    }

}
