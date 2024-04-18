package main.java.logic.translation.mode;

import java.util.Properties;

import main.java.logic.file.TargetFile;

/**
 * Manual translation mode. Virtually does nothing, as user interacts directly
 * with IDE to create the translation.
 * 
 * 
 * @author Adriana R.F. (uo282798)
 * @version April 2024
 */
public class ManualTranslation implements TranslationMode {

    /**
     * Writes property keys into a new file so that the user can execute the
     * manual translation themselves.
     * 
     * @throws Exception in case of issue with I/O writing to file
     */
    @Override
    public Properties translate(TargetFile f) throws Exception {
	return null; // Nothing to save!
    }

    /**
     * Resets the cache and eliminates all of its records.
     */
    @Override
    public void reset() {
	// Nothing to do!
    }

}
