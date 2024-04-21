package main.java.logic.translation.mode;

import java.sql.SQLException;
import java.util.Properties;

import main.java.logic.file.SourceFile;
import main.java.logic.file.TargetFile;
import main.java.logic.translation.api.ApiTranslation;
import main.java.logic.translation.api.openai.OpenAIApiTranslation;
import main.java.logic.translation.cache.TranslationCache;
import main.java.logic.util.exception.TranslationException;
import main.java.logic.util.properties.PropertiesUtil;

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

    public AutoTranslation(SourceFile source) throws Exception {
	this.api = new OpenAIApiTranslation(); // API access
	this.cache = new TranslationCache(); // Translation database
	this.source = source;
    }

    /**
     * Executes the automatic API translation process, from a given input file:
     * 
     * - 1. Establishes the target language.
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
     * @throws Exception if there is an error with API access
     */
    @Override
    public Properties translate(TargetFile target) throws Exception {
	// New current target file
	this.target = target;

	// Checks whether some translations have already been made
	fromCache();
	// Translate strictly those that have never been translated before
	Properties results = getAutoResults(target.getTargetLanguage());
	target.setResults(results);
	// Update cache
	toCache();
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
     * @throws TranslationException
     * 
     * @throws Exception            in case of issue with DB
     */
    private void toCache() throws Exception {
	if (api.getResults() != null) {
	    cache.storeAll(api.getResults(), target.getContent(),
		    target.getTargetCode());
	}
    }

    /**
     * Retrieves those values already translated and present in the database,
     * and sets the group of properties to be translated and sent to the API.
     * 
     * @throws Exception in case of issue with DB
     */
    private void fromCache() throws Exception {
	cache.match(source.getContent(), target.getTargetCode());
    }

    /**
     * Considering the translations found in the cache and those yet to be done,
     * it either does/doesn't request translations to the API, for optimization
     * of performance.
     * 
     * @param language format "English, United States"
     * @return combined results (cache, API...)
     * @throws Exception in case of error with API translation
     */
    private Properties getAutoResults(String language)
	    throws TranslationException {

	// No need to access the API
	if (cache.getUntranslated().isEmpty()) {
	    return cache.getTranslated();

	} else {
	    // Needs to access the API
	    api.translate(cache.getUntranslated(), language);

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

}
