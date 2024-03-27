package main.java.util.file;

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
     * Merges 2 properties objects.
     * 
     * @param p1
     * @param p2
     * @return merged properties into one object
     */
    public static Properties join(Properties p1, Properties p2) {
	Properties p = new Properties(p2);
	p.forEach((key, value) -> p1.setProperty(key.toString(),
		value.toString()));
	return p;
    }

    /**
     * @param properties object
     * @return list of the keys of the object
     */
    public static List<String> getKeys(Properties pr) {
	Enumeration<Object> keys = pr.keys();
	List<String> k = new ArrayList<String>();

	while (keys.hasMoreElements()) {
	    k.add((String) keys.nextElement());
	}

	return k;
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
