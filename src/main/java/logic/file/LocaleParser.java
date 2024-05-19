package main.java.logic.file;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import main.java.util.exception.ResourceException;
import main.java.util.properties.ResourceLoader;

/**
 * [UTILITY CLASS]
 * 
 * Parser for localization alpha-2 codes and language names.
 * 
 * Includes functionalities to extract/derive language-country mappings, locale
 * names for i18n files, display names/codes for locale objects, and other
 * processing functionalities in terms of language names and codes.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public class LocaleParser {

    // Language-index-locale mappings
    private static Map<String, Locale> map;
    private static Map<String, Integer> localeLanguages = new HashMap<>();
    private static Map<Integer, String> englishLanguages = new HashMap<>();

    public LocaleParser(ResourceBundle messages) throws ResourceException {
	map = getMap();
	localeLanguages = ResourceLoader.getMapSupportedLanguages(messages);
	englishLanguages = ResourceLoader.getMapSupportedLanguages_English();
    }

    /*
     * ######################## NAMES/CODES METHODS ############################
     */

    /**
     * From the index of an input of the format "German, Germany", extract the
     * locale codes, i.e. "de_DE".
     * 
     * @param language display name also containing the country name
     * @return locale object representing the input
     */
    public Locale getLocale(String language) {

	String input = getTranslatedLanguage(language);
	String[] parts = input.split(", ");
	String lang = parts[0].trim().toLowerCase();
	String country = parts[1].trim().toLowerCase();

	// Create a Locale object with language and country
	return map.get(lang + "-" + country);
    }

    /**
     * @param locale object with a specific language and country code
     * @return alpha2 code formatted string, specifying language and country
     */
    public String getCode(Locale locale) {
	String language = locale.getLanguage();
	if (language.contains("_")) {
	    return language;
	}
	return (locale.getLanguage() + "-" + locale.getCountry()).toLowerCase();
    }

    /**
     * @param locale object with a specific language and country code
     * @return display language
     */
    public String getLanguage(Locale locale) {
	Locale def = Locale.getDefault();
	Locale.setDefault(new Locale("en_US"));
	String language = locale.getDisplayLanguage();
	Locale.setDefault(def);
	return language;
    }

    /**
     * From an input of the format "de_DE", extract the language name.
     * 
     * @param input containing "langCode-COUNTRYCODE"
     * @return String representing the display language of a given alpha-2 code
     */
    public String getLanguage(String input) {

	// Create a Locale object with the provided locale code
	Locale locale = new Locale(input);

	// Get the display name of the language
	String languageName = locale.getDisplayLanguage();

	return languageName;
    }

    /*
     * ######################## FILENAMES METHODS ############################
     */

    /**
     * From a given file path, extracts the bundle name and specific locale
     * alpha-2 code.
     * 
     * @param file path (absolute path to file)
     * @return string[] (containing bundle name and language-country alpha-2
     *         code)
     */
    public String[] unformat(String filepath) {
	int extension = filepath.lastIndexOf('.');
	int name = filepath.lastIndexOf("\\");
	String bundleName = filepath.substring(name + 1, extension);
	String codes;

	String[] divided = bundleName.split("_", 2);

	if (divided.length == 2) {
	    // Extract the base name and locale
	    bundleName = divided[0];
	    codes = divided[1];

	    return new String[] { bundleName, codes };
	} else if (divided.length == 1) {
	    return new String[] { bundleName, null };
	}

	return new String[] { null, null };
    }

    /*
     * ######################## AUXILIARY METHODS ##############################
     */

    /**
     * Builds a mapping between all currently-supported languages, with the
     * format "language-country" being the key.
     * 
     * The keys should be in English, that is: · "albanian-albania" and not: ·
     * "albanes-albania"
     * 
     * @return map between language-country and its Locale object
     */
    private Map<String, Locale> getMap() {
	Map<String, Locale> map = new HashMap<String, Locale>();
	String key;

	Locale def = Locale.getDefault();
	Locale.setDefault(new Locale("en_US"));
	for (Locale locale : Locale.getAvailableLocales()) {
	    key = locale.getDisplayLanguage().toLowerCase() + "-"
		    + locale.getDisplayCountry().toLowerCase();
	    map.put(key, locale);
	}
	Locale.setDefault(def);

	return map;
    }

    /**
     * From the index of the app-localized language, return the specific string
     * in English.
     * 
     * @param language in the program's locale at the moment
     * @return name of that language+country in English @
     */
    private String getTranslatedLanguage(String language) {
	int index = localeLanguages.get(language);
	return englishLanguages.get(index).toLowerCase();
    }

}
