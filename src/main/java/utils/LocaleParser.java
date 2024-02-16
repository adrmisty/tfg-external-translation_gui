package main.java.utils;

import java.util.Locale;

public class LocaleParser {

    /*
     * From an input of the format "German, Germany", extract the locale codes,
     * i.e. "de_DE".
     * 
     * @param string containing "language, Country"
     * 
     * @return locale object representing the input
     */
    public static Locale extract(String input) throws Exception {
	String[] parts = input.split(",\\s*");

	if (parts.length == 2) {
	    String language = parts[0].trim();
	    String country = parts[1].trim();

	    // Create a Locale object with language and country
	    Locale locale = new Locale(language, country);

	    return locale;
	} else {
	    throw new Exception("That locale choice is not supported yet!");
	}
    }

    /**
     * From an input of the format "de_DE", extract the language name.
     * 
     * @param string containing "langCode_COUNTRYCODE"
     * @return String
     */
    public static String getLanguage(String input) {

	// Create a Locale object with the provided locale code
	Locale locale = new Locale(input);

	// Get the display name of the language
	String languageName = locale.getDisplayLanguage();

	return languageName;
    }

}
