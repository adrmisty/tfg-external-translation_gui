package main.java.util.file;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import main.java.util.LocaleNameParser;
import main.java.util.ResourceLoader;

/**
 * Processes a localization file (its content, name and format) so that its
 * information is readily available for translation.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public class LocaleFileProcessor {

    // Locale
    private static final String _DEFAULT_CODE = Locale.getDefault()
	    .toLanguageTag();

    // Language-index-locale mappings
    private static Map<String, Locale> map;
    private static Map<String, Integer> localeLanguages = new HashMap<>();
    private static Map<Integer, String> englishLanguages = new HashMap<>();

    public LocaleFileProcessor(ResourceBundle messages) throws Exception {
	setMappings(messages);
    }

    /**
     * Check whether a file is compliant with the i18n format for localization,
     * and retrieves its related information (bundle name + localization codes
     * of the source language it is written in).
     * 
     * @param filePath of the .properties file
     * @return mapping of file information (bundle name + locale codes in
     *         "xx_XX" format).
     * @throws Exception
     */
    public Map<String, String> getFileInfo(String filepath) throws Exception {

	if (!ResourceLoader.getFileExtension(filepath).get()
		.equals("properties")) {
	    throw new Exception(
		    "The indicated localization file is not i18n-compliant!");
	}

	String[] fileName = LocaleNameParser.extractLocaleFromFile(filepath);
	String bundleName = fileName[0];
	String codes = fileName[1];

	if (codes == null) {
	    codes = _DEFAULT_CODE;
	}

	// Save bundle name and its locale
	Map<String, String> fileInfo = new HashMap<String, String>();
	fileInfo.put("bundleName", bundleName);
	fileInfo.put("locales", codes);

	return fileInfo;
    }

    /**
     * From a given localized string specifying a target language, retrieve its
     * assigned Locale object.
     * 
     * @param bundle    of localized messages
     * @param localized target language as a string
     * @return locale object
     * @throws Exception in case of unavailable target locale
     */
    public Locale getTargetLanguage(String language) throws Exception {
	return LocaleNameParser.extract(map, localeLanguages, englishLanguages,
		language);
    }

    /**
     * From the texts given as results, build the .properties-like text to be
     * written onto the file.
     * 
     * @param targetLanguage: string specifying the target language of this
     *                        translation
     * @param text:           different translations returned by the translator
     * @param properties:     properties object with key/vaue pairs
     * @return complete text in the specific format
     */
    public StringBuilder getWrittenResults(String targetLanguage, String text,
	    Properties props) {
	String[] sentences = text.split("\n");
	StringBuilder sb = new StringBuilder("");
	sb.append("# " + targetLanguage + "\n\n");

	int i = 0;
	String p;
	Enumeration<Object> keys = props.keys();

	while (keys.hasMoreElements()) {
	    if (i < sentences.length) {
		p = ((String) keys.nextElement()) + "=" + sentences[i];
		sb.append(p + "\n");
	    } else {
		break;
	    }
	    i++;
	}
	return sb;
    }

    /**
     * From the texts given as results, build the .properties-like text to be
     * written onto the file.
     * 
     * @param targetLanguage: string specifying the target language of this
     *                        translation
     * @param text:           different translations returned by the translator
     * @return succession of keys in the file
     */
    public StringBuilder getKeysText(String targetLanguage, Properties props) {
	StringBuilder sb = new StringBuilder("");
	sb.append("# " + targetLanguage + "\n\n");

	Enumeration<Object> keys = props.keys();

	while (keys.hasMoreElements()) {
	    sb.append((String) keys.nextElement() + "=\n");
	}
	return sb;
    }

    /**
     * Establishes the mappings between index and meaning of the different
     * languages.
     * 
     * @param messages: localized messages in the program's locale
     * @throws Exception
     */
    private void setMappings(ResourceBundle messages) throws Exception {
	map = LocaleNameParser.getMap();
	localeLanguages = ResourceLoader.getMapSupportedLanguages(messages);
	englishLanguages = ResourceLoader.getMapSupportedLanguages_English();
    }
}
