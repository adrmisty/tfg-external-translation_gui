package main.java.logic.util.file;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

import main.java.logic.util.exception.PropertiesException;
import main.java.logic.util.properties.PropertiesUtil;

/**
 * Processes a localization file (its content, name and format) so that its
 * information is readily available and saved for translation. It also deals
 * with the format a .properties file should have when it comes to writing to
 * file,
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public class LocaleFileProcessor {

    // Language parser (codes + names functions)
    private static LanguageParser parser;

    public LocaleFileProcessor(ResourceBundle messages) throws Exception {
	parser = new LanguageParser(messages);
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

	if (!getFileExtension(filepath).get().equals("properties")) {
	    throw new PropertiesException(
		    "The indicated localization file is not i18n-compliant!",
		    filepath);
	}

	String[] fileName = parser.unformat(filepath);
	String bundleName = fileName[0];
	String codes = fileName[1];

	if (codes == null) {
	    codes = parser.getCode(Locale.getDefault());
	}

	// Save bundle name and its locale
	Map<String, String> fileInfo = new HashMap<String, String>();
	fileInfo.put("bundleName", bundleName);
	fileInfo.put("locale", parser.getLanguage(codes));

	return fileInfo;
    }

    /**
     * From a given localized string specifying a target language, retrieve its
     * assigned Locale object.
     * 
     * @param language string representing language, format "English, United
     *                 States"
     * @return locale object representing that language, with format code
     *         "en_US"
     * @throws Exception in case of unavailable target locale
     */
    public Locale getLanguage(String language) throws Exception {
	return parser.getLocale(language);
    }

    /**
     * @param target locale object representing language
     * @return display name of the language in the app's locale
     * @throws Exception in case of unavailable language
     */
    public String getLanguage(Locale locale) throws Exception {
	return parser.getLanguage(locale);
    }

    /**
     * @param target locale object representing language
     * @return locale alpha2 string code representing that language
     * @throws Exception in case of unavailable language
     */
    public String getCode(Locale locale) throws Exception {
	return parser.getCode(locale);
    }

    /**
     * Establishes the format for a localization file name.
     * 
     * @param filename of the new locale file
     * @return formatted filename
     */
    public String format(String filename) {
	return parser.format(filename);
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
    public StringBuilder getValuesText(String targetLanguage,
	    Properties texts) {
	StringBuilder sb = new StringBuilder("");
	sb.append("# " + targetLanguage + "\n\n");

	String p;
	List<String> keys = PropertiesUtil.getKeys(texts);
	List<String> result = PropertiesUtil.getValues(texts);

	for (int i = 0; i < keys.size(); i++) {
	    p = keys.get(i) + "=" + result.get(i);
	    sb.append(p);
	    if (i < texts.size() - 1) {
		sb.append("\n");
	    }
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

	List<String> keys = PropertiesUtil.getKeys(props);

	for (String k : keys) {
	    sb.append(k + "=\n");

	}
	return sb;
    }

    /**
     * @param path absolute path to a file
     * @return file extension of the file, if it exists
     */
    public static Optional<String> getFileExtension(String path) {
	return Optional.ofNullable(path).filter(f -> f.contains("."))
		.map(f -> f.substring(path.lastIndexOf(".") + 1));
    }

}
