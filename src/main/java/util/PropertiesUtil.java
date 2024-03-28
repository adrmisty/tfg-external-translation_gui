package main.java.util;

import java.util.ArrayList;
import java.util.Collection;
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
     * Merges 2 properties objects.
     * 
     * @param p1
     * @param p2
     * @return merged properties into one object
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
     * @param properties object
     * @return list of the keys of the object
     */
    public static List<String> getKeys(Properties pr) {
	return new ArrayList<String>(pr.stringPropertyNames());
    }

    /**
     * @param properties object
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

}
