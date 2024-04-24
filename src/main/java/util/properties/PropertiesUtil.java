package main.java.util.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Utilities method with regards to Properties objects, their keys and values.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public class PropertiesUtil {

    /**
     * Replaces the values of a given Properties object with those given as
     * input.
     * 
     * @param properties properties object with respective keys and values
     * @param values     texts to replace Properties values with
     * 
     * @return properties object containing replacement of property values for
     *         their translations
     */
    public static Properties replaceValues(Properties properties,
	    String values) {
	String[] res = values.split("\n");
	Properties replaced = new Properties();
	Enumeration<Object> keys = properties.keys();
	int i = 0;

	while (keys.hasMoreElements()) {
	    String key = (String) keys.nextElement();
	    replaced.put(key, res[i]);
	    i++;
	}
	return replaced;
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

}
