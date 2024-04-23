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
     * Does not carry out a translation process. Returns the properties found in
     * its source file, since the only usable information from said file will be
     * the keys themselves.
     * 
     * @param f target file to translate
     * @return content of its source file
     * 
     */
    @Override
    public Properties translate(TargetFile f) throws Exception {
	f.setResults(null);
	return f.getContent(); // Nothing to save!
    }

    /**
     * Resets the cache and eliminates all of its records.
     */
    @Override
    public void reset() {
	// Nothing to do!
    }

}
