package main.java.logic.translation.api;

import java.util.Properties;

import main.java.util.exception.TranslationException;

/**
 * Interface for API access aimed at automatic translation.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public interface ApiTranslation {

    /**
     * Translates a given prompt (containing localization texts that the user
     * has given as input) into a specific language also specified in the prompt
     * via a set of requests to the API.
     * 
     * @param properties Properties object containing i18n localization settings
     *                   with texts in a given language
     * @param sourceLang i.e. "Palestinian (Palestine)"
     * @param targetLang i.e. "German (Germany)"
     * @return translated texts as a string
     * 
     * @return properties object with the parameter properties translated onto
     *         the target language
     * @throws Exception in case of issue with API access and request
     */
    public Properties translate(Properties properties, String sourceLang,
	    String targetLang) throws TranslationException;

    /**
     * Retrieves the translation results returned by the API.
     * 
     * @return properties object containing translations computed and retrieved
     *         from the API
     */
    public Properties getResults();
}
