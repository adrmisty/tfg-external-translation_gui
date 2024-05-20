package main.java.logic.translation.mode;

import java.sql.SQLException;
import java.util.Properties;

import main.java.logic.file.SourceFile;
import main.java.logic.file.TargetFile;
import main.java.logic.translation.api.ApiTranslation;
import main.java.logic.translation.api.openai.OpenAIApiTranslation;
import main.java.logic.translation.cache.TranslationCache;
import main.java.util.exception.ResourceException;
import main.java.util.exception.TranslationException;
import main.java.util.properties.PropertiesUtil;

/**
 * Automatic translation mode (translates content to a specific language via
 * requests to a LLM), which has access to a translation cache with past
 * translation results.
 * 
 * @author Adriana R.F. (uo282798)
 * @version March 2024
 */
public class AutoTranslation implements TranslationMode {

    private ApiTranslation api;
    private TranslationCache cache;

    // Source file
    private SourceFile source;
    // Current
    private TargetFile target;

    public AutoTranslation(SourceFile source)
	    throws ResourceException, SQLException {
	this.api = new OpenAIApiTranslation(); // API access
	this.cache = new TranslationCache(); // Translation database
	// this.cache.reset();
	this.source = source;
    }

    /**
     * Executes the automatic API translation process, from a given input file:
     * 
     * - 1. Establishes the target language. (In the case that it is the same as
     * the source one, no translation process is done!)
     * 
     * - 2. Accesses LL to carry out automatic translation. (In case of
     * translations having already been carried out in the past, result is
     * retrieved directly from the caché).
     * 
     * - 3. Retrieves results.
     * 
     * - 4. Updates caché with new translations.
     * 
     * @param target target file to translate
     * @return results of automatic translation
     * 
     * @throws TranslationException as a result of issues with translation API
     *                              access, timeouts, interruptions
     */
    @Override
    public Properties translate(TargetFile target) throws TranslationException {
	// New current target file
	this.target = target;
	Properties results;

	// If they are the same language, return the same content
	if (areSameLanguage(source, target)) {
	    results = source.getContent();
	    target.setResults(results);
	} else {
	    // Checks whether some translations have already been made
	    fromCache();
	    // Translate strictly those that have never been translated before
	    results = getAutoResults(source.getLanguage(),
		    target.getTargetLanguage());
	    target.setResults(results);
	    // Update cache
	    toCache();
	}

	// Retrieve results
	return results;
    }

    /**
     * Resets the cache and eliminates all of its records.
     */
    @Override
    public void reset() {
	try {
	    cache.reset();
	    source = null;
	    target = null;
	} catch (SQLException e) {
	    // Do nothing
	}
    }

    /**
     * If any, saves API results onto translation cache (if not found in the
     * database!).
     * 
     * @throws TranslationException as a result of issues with translation API
     *                              access, timeouts, interruptions
     * 
     */
    private void toCache() throws TranslationException {
	if (api.getResults() != null) {
	    cache.storeAll(api.getResults(), target.getContent(),
		    target.getTargetCode());
	}
    }

    /**
     * Retrieves those values already translated and present in the database,
     * and sets the group of properties to be translated and sent to the API.
     * 
     * @throws TranslationException in case of error when accessing the database
     *                              or retrieving past translations
     */
    private void fromCache() throws TranslationException {
	cache.match(source.getContent(), target.getTargetCode());
    }

    /**
     * Considering the translations found in the cache and those yet to be done,
     * it either does/doesn't request translations to the API, for optimization
     * of performance.
     * 
     * @param sourceLang format "Arabic (Palestine)" or "Arabic"
     * @param language   format "English (United Kingdom)" or "English"
     * @return combined results (cache, API...) @ in case of error with API
     *         translation
     * 
     * @throws TranslationException as a result of issues with translation API
     *                              access, timeouts, interruptions
     */
    private Properties getAutoResults(String sourceLang, String language)
	    throws TranslationException {

	// No need to access the API
	if (cache.getUntranslated().isEmpty()) {
	    return cache.getTranslated();

	} else {
	    // Needs to access the API
	    api.translate(cache.getUntranslated(), sourceLang, language);

	    // Everything has been translated from API
	    if (cache.getTranslated().isEmpty()) {
		return api.getResults();

		// Both API and cache translations
	    } else {
		return PropertiesUtil.join(api.getResults(),
			cache.getTranslated());
	    }
	}

    }

    /**
     * @param source source file
     * @param target target file
     * @return boolean true if they are in the same language and region
     */
    private boolean areSameLanguage(SourceFile source, TargetFile target) {
	String src = source.getLanguage();
	String tgt = target.getTargetLanguage().replace("(", " from ")
		.replace(")", "").toLowerCase();

	if (!src.equals(tgt)) // Not completely equal
	{
	    if (!tgt.contains(" ")) { // Target is global
		return src.contains(tgt);
	    }
	    return false;
	}
	return true;
    }
}
