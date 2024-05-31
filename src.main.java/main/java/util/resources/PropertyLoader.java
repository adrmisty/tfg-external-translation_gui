package main.java.util.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import main.java.util.exception.PropertiesException;

/**
 * Utilities method with regards to Properties objects and .properties files.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
public class PropertyLoader {

    /**
     * Parses a .properties file content onto a Properties object.
     * 
     * 
     * @param file      path of the .properties file to load
     * @param forReview whether this loading is done in a temporary-review file
     * 
     * @return Properties object containing the contents of the file
     * @throws IOException
     * @throws PropertiesException if there is an error while loading properties
     *                             from a given file (formatting/content errors
     *                             inside file)
     */
    public static Properties load(String filepath, boolean forReview)
	    throws PropertiesException, IOException {
	if (isI18N(filepath, forReview)) {
	    Properties props = new Properties();
	    File file = new File(filepath);

	    // Ensure all alphabets are parsed correctly
	    try (FileInputStream fileStream = new FileInputStream(file);
		    InputStreamReader reader = new InputStreamReader(fileStream,
			    "UTF-8")) {
		props.load(reader); // Ignores comments, etc
	    }
	    return props;
	} else {
	    throw new PropertiesException(filepath, true);
	}
    }

    /**
     * Builds a properties object from a text that should have a Properties-like
     * format.
     * 
     * @param values texts to build key-value pairs with
     * 
     * @return properties object containing replacement of property values for
     *         their translations
     */
    public static Properties getProperties(String values) {
	Properties props = new Properties();
	String[] res = values.split("\n");
	String[] split;
	String key;
	String value;

	for (int i = 0; i < res.length; i++) {
	    split = res[i].split("=", 2);
	    key = split[0];
	    try {
		value = split[1];
	    } catch (Exception e) {
		value = "";
	    }

	    props.put(key, value);
	}
	return props;
    }

    /**
     * Merges 2 properties objects.
     * 
     * @param p1 properties object 1
     * @param p2 properties object 2
     * @return merged properties
     */
    public static Properties join(Properties p1, Properties p2) {
	Properties mergedProperties = new Properties();

	// Copy properties from prop1 to mergedProperties
	for (String key : p1.stringPropertyNames()) {
	    mergedProperties.setProperty(key, p1.getProperty(key));
	}

	// Copy properties from prop2 to mergedProperties
	for (String key : p2.stringPropertyNames()) {
	    mergedProperties.setProperty(key, p2.getProperty(key));
	}

	return mergedProperties;
    }

    /**
     * Retrieves all the keys found in a Properties object.
     * 
     * @param pr properties object
     * @return list of the keys of the object
     */
    public static List<String> getKeys(Properties pr) {
	return new ArrayList<String>(pr.stringPropertyNames());
    }

    /**
     * Retrieves all the values found in a Properties object.
     * 
     * @param pr properties object
     * @return list of the values of the object
     */
    public static List<String> getValues(Properties pr) {
	Collection<Object> values = pr.values();
	List<String> v = new ArrayList<String>();
	Iterator<Object> it = values.iterator();

	while (it.hasNext()) {
	    v.add((String) it.next());
	}
	return v;
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
    public static StringBuilder getKeysText(String targetLanguage,
	    Properties props) {
	StringBuilder sb = new StringBuilder("");
	sb.append("# " + targetLanguage + "\n\n");

	List<String> keys = getKeys(props);

	for (String k : keys) {
	    sb.append(k + "=\n");

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
     * @param properties:     properties object with key/vaue pairs
     * @return complete text in the specific format
     */
    public static StringBuilder getValuesText(String targetLanguage,
	    Properties texts) {
	StringBuilder sb = new StringBuilder("");
	sb.append("# " + targetLanguage + "\n\n");

	String p;
	List<String> keys = PropertyLoader.getKeys(texts);
	List<String> result = PropertyLoader.getValues(texts);

	for (int i = 0; i < keys.size(); i++) {
	    p = keys.get(i) + "=" + result.get(i);
	    sb.append(p);
	    if (i < texts.size() - 1) {
		sb.append("\n");
	    }
	}
	return sb;
    }

    // Auxiliary

    /**
     * @param file path of input file
     * @return boolean true if input file is a .properties file with i18n
     *         format, false otherwise
     * @throws IOException
     * @throws FileNotFoundException
     */
    private static boolean isI18N(String filePath, boolean forReview)
	    throws FileNotFoundException, IOException {

	try (BufferedReader reader = new BufferedReader(
		new FileReader(filePath))) {
	    String line;
	    while ((line = reader.readLine()) != null) {

		// Ignore comments and empty lines
		if (line.trim().isEmpty() || line.trim().startsWith("#")) {
		    continue;
		}

		// Check if the line is in key-value format
		if (!line.contains("=")) {
		    return false;
		}

		// Check if the key and value are separated by '='
		String[] parts = line.split("=", 2);
		if (!forReview) {
		    if (parts.length != 2 || parts[0].trim().isEmpty()
			    || parts[1].trim().isEmpty()) {
			return false;
		    }
		} else {
		    // Value can be empty
		    if (parts.length != 2 || parts[0].trim().isEmpty()) {
			return false;
		    }
		}
	    }
	}

	// All good, it's a fully-formed properties file!
	return true;
    }

}
