package main.java.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

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
    private static Map<String, Locale> map;

    public LocaleFileProcessor() {
	map = LocaleNameParser.getMap();
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
     * From a given string specifying a target language, retrieve its assigned
     * Locale object.
     * 
     * @param target language as a string
     * @return locale object
     * @throws Exception in case of unavailable target locale
     */
    public Locale getTargetLanguage(String target) throws Exception {
	return LocaleNameParser.extract(map, target);
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
	    p = ((String) keys.nextElement()) + "=" + sentences[i];
	    sb.append(p + "\n");
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

}
