package main.java.logic.file.locales;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import main.java.util.ResourceLoader;
import main.java.util.exception.ResourceException;

/**
 * Parser for localization alpha-2 codes and language names.
 * 
 * Includes functionalities to extract/derive language-country mappings, locale
 * names for i18n files, display names/codes for locale objects, and other
 * processing functionalities in terms of language names and codes.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
public class LocaleParser {

    // Language-index-locale mappings
    private static Map<String, Locale> map;
    private static Map<String, Integer> localeLanguages = new HashMap<>();
    private static Map<Integer, String> englishLanguages = new HashMap<>();

    public LocaleParser(ResourceBundle messages) throws ResourceException {
	map = getMap();
	localeLanguages = ResourceLoader.getMapSupportedLanguages(messages);
	englishLanguages = ResourceLoader.getMapSupportedLanguagesInEnglish();
    }

    /**
     * From a string indicating a language or language-region, retrieve its
     * alpha2 code.
     * 
     * @param language name of the language, either global "German" or
     *                 region-specific "German, Austria"
     * @return locale object representing the input locale
     */
    public Locale getLocale(String language) {

	String input = getLanguageInEnglish(language).toLowerCase();
	String[] specific = input.split(", ");

	if (specific.length == 1) {
	    // Global
	    return map.get(input);

	} else {
	    // Region-specific
	    String lang = specific[0].trim().toLowerCase();
	    String country = specific[1].trim().toLowerCase();
	    return map.get(lang + "-" + country);
	}
    }

    /**
     * From a locale object, retrieve its language global/specific
     * representative string in English.
     * 
     * @param locale object containing a specific localization code
     * @return a string of the format "Language (Country)" or "Language".
     */
    public String getLanguage(Locale locale) {
	String language = getLanguage(getCode(locale));
	String country = getCountry(locale);
	if (!country.isBlank()) {
	    return language + "(" + country + ")";
	}
	return language;
    }

    /**
     * From a locale object, retrieve its alpha2 code.
     * 
     * @param locale object with a global language or specific language-country
     *               localization
     * @return alpha2 code with format "language-country" or "language",
     *         depending on whether specific or global
     */
    public String getCode(Locale locale) {
	return locale.toLanguageTag();
    }

    /**
     * From a locale object, retrieve its display country name in English.
     * 
     * @param locale object with a global language or specific language- country
     *               localization code
     * @return display country in English, if any
     */
    public String getCountry(Locale locale) {
	Locale def = Locale.getDefault();
	Locale.setDefault(new Locale("en_US"));
	String language = locale.getDisplayCountry();
	Locale.setDefault(def);
	return language;
    }

    /**
     * From an alpha2 localization code, extract its language name in the
     * program's locale.
     * 
     * @param inputCode alpha2 code, either global "language" or specific
     *                  "language-country"
     * @return display language of the country code, in the app's language
     */
    public String getLanguage(String inputCode) {

	// Map processing
	inputCode = inputCode.replace("_", "-");
	String key = "";
	String code = "";
	Locale value;

	for (Map.Entry<String, Locale> entry : map.entrySet()) {
	    key = entry.getKey();

	    if (key.isBlank()) {
		continue;
	    }

	    value = entry.getValue();
	    code = value.toLanguageTag();
	    if (code.equals(inputCode)) {
		break;
	    }
	}

	String[] parts = key.split("-");
	if (parts.length == 1) {
	    return StringUtils.capitalize(key);
	} else {
	    return (StringUtils.capitalize(parts[0]) + " ("
		    + StringUtils.capitalize(parts[1]) + ")");
	}

    }
    /*
     * ######################## AUXILIARY METHODS ##############################
     */

    /**
     * Builds a mapping between all currently-supported languages, with the
     * format "language-country" being the key, in English.
     * 
     * @return map between "language"/"language-country" and its Locale object
     */
    private Map<String, Locale> getMap() {
	Map<String, Locale> map = new HashMap<String, Locale>();

	// key: "lang-country" or "lang"
	String key;
	String lang;
	String country;
	Locale value;

	// All keys will be in English; user can change the app's locale
	Locale def = Locale.getDefault();
	Locale.setDefault(new Locale("en_US"));
	for (Locale locale : Locale.getAvailableLocales()) {
	    lang = locale.getDisplayLanguage().toLowerCase();
	    country = locale.getDisplayCountry().toLowerCase();

	    // Global locale
	    if (country.isBlank()) {
		key = lang;
		value = new Locale(locale.getLanguage());
		// Region-specific locale
	    } else {
		key = lang + "-" + country;
		value = locale;
	    }
	    map.put(key, value);
	}
	Locale.setDefault(def);
	return map;
    }

    /**
     * From a string indicating a language or language-region, extract its
     * locale object.
     * 
     * @param language of format "German" or "German, Germany" in the app's
     *                 current locale
     * @return translation into English
     */
    private String getLanguageInEnglish(String language) {
	int index = localeLanguages.get(language);
	return englishLanguages.get(index);
    }

}
