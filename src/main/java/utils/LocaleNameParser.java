package main.java.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Localization alpha-2 codes and names parser for files. Includes
 * functionalities to extract/derive language-country mappings, locale names for
 * i18n files and other processing functionalities in terms of file names.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public class LocaleNameParser {

    /**
     * Builds a mapping between all currently-supported languages, with the
     * format "language-country" being the key.
     * 
     * @return map between language-country and its Locale object
     */
    public static Map<String, Locale> getMap() {
	Map<String, Locale> map = new HashMap<String, Locale>();
	String key;

	for (Locale locale : Locale.getAvailableLocales()) {
	    key = locale.getDisplayLanguage().toLowerCase() + "-"
		    + locale.getDisplayCountry().toLowerCase();
	    map.put(key, locale);
	}
	return map;
    }

    /**
     * From an input of the format "German, Germany", extract the locale codes,
     * i.e. "de_DE".
     * 
     * @param string containing "language, Country"
     * @return locale object representing the input
     */
    public static Locale extract(Map<String, Locale> map, String input)
	    throws Exception {
	String[] parts = input.split(", ");

	if (parts.length == 2) {
	    String language = parts[0].trim().toLowerCase();
	    String country = parts[1].trim().toLowerCase();

	    // Create a Locale object with language and country
	    return map.get(language + "-" + country);
	} else {
	    throw new Exception("That locale choice is not supported yet!");
	}
    }

    /**
     * From an input of the format "de_DE", extract the language name.
     * 
     * @param string containing "langCode_COUNTRYCODE"
     * @return String representing the display language of a given alpha-2 code
     */
    public static String getLanguage(String input) {

	// Create a Locale object with the provided locale code
	Locale locale = new Locale(input);

	// Get the display name of the language
	String languageName = locale.getDisplayLanguage();

	return languageName;
    }

    /**
     * From a given file path, extracts the bundle name and specific locale
     * alpha-2 code.
     * 
     * @param file path (absolute path to file)
     * @return string[] (containing bundle name and language-country alpha-2
     *         code)
     */
    public static String[] extractLocaleFromFile(String filepath) {
	int extension = filepath.lastIndexOf('.');
	int name = filepath.lastIndexOf("\\");
	String bundleName = filepath.substring(name + 1, extension);
	String codes;
	int last_ = filepath.lastIndexOf('_');

	if (last_ > 0 && last_ < filepath.length() - 1) {
	    // Extract the base name and locale
	    bundleName = filepath.substring(name + 1, last_);
	    codes = filepath.substring(last_ + 1, extension);

	    return new String[] { bundleName, codes };
	} else if (last_ == -1) {
	    return new String[] { bundleName, null };
	}

	return new String[] { null, null };
    }

    /**
     * Using regular expressions, eliminate those instances in locale filenames
     * of the type: "-Latn", "-Cyrl", "-Geor"...
     * 
     * @param filename to remove the possible substring from
     * @return filename without that alphabet indication
     */
    public static String removeAlphabetType(String filename) {
	Pattern pattern = Pattern.compile("-[A-Za-z]+(?=-[A-Za-z]{2})");
	Matcher matcher = pattern.matcher(filename);

	// Replace matched instances with an empty string
	String result = matcher.replaceAll("");

	return result;
    }

}
