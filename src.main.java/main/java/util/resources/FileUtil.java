package main.java.util.resources;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.java.util.exception.PropertiesException;

/**
 * Used to extract information from files.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
public class FileUtil {

    private static final String PROPERTIES_FILE_REGEX = "^[a-zA-Z0-9_/.-]+(_[a-z]{2}(_[A-Z]{2})?)?\\.properties$";

    /**
     * Eliminate those instances in locale filenames of the type: "-Latn",
     * "-Cyrl", "-Geor"... Also, replaces all hyphens with underscores so that
     * resulting filename complies with format.
     * 
     * @param filename to remove the possible substring from
     * @return filename without that alphabet indication
     */
    public static String format(String filename) {
	Pattern pattern = Pattern.compile("-[A-Za-z]+(?=-[A-Za-z]{2})");
	Matcher matcher = pattern.matcher(filename);

	// Replace matched instances with an empty string
	String result = matcher.replaceAll("");

	// Uppercase last part of code
	result = result.replace("-", "_");
	String[] split = result.split("_");

	// Info
	String bundle = split[0];
	String lang = "";
	String country = "";

	if (split.length == 1) {
	    return bundle;
	} else {
	    lang = split[1];
	    if (split.length > 2) {
		country = "_" + split[2].toUpperCase();
		return bundle + "_" + lang + country;
	    } else {
		return bundle + "_" + lang;
	    }

	}
    }

    /**
     * From a given file path, extracts the bundle name and specific locale
     * alpha-2 code.
     * 
     * @param file path (absolute path to file)
     * @return string array containing bundle name and, optionally, a
     *         language/language-country alpha-2 code
     */
    public static String[] unformat(String filepath) {
	int extension = filepath.lastIndexOf('.');
	int name = filepath.lastIndexOf("/");
	if (name < 0) {
	    name = filepath.lastIndexOf("\\");
	}
	if (!filepath.substring(name + 1).matches(PROPERTIES_FILE_REGEX)) {
	    throw new PropertiesException(filepath);
	}
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

}
