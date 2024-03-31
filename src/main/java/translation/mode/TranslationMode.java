package main.java.translation.mode;

import java.util.Properties;

import main.java.util.exception.TranslationException;

/**
 * Translation mode (at the moment, either automatic or manual).
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public interface TranslationMode {

    /**
     * Translates a specific set of Properties to a given language.
     * 
     * @param language format "English, United States"
     * @return properties translated to another language
     * @throws TranslationException in case of issues during the translation
     *                              process
     * @throws Exception            in case of issues during the translation
     *                              process
     */
    public Properties translate(String language) throws Exception;

    /**
     * Interacts with target file processor to write results.
     * 
     * @throws Exception in case of issue with I/O writing
     */
    public void write() throws Exception;

    /**
     * Resets all associated information in this translation mode.
     */
    public void reset();
}
