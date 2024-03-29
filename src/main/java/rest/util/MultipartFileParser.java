package main.java.rest.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.web.multipart.MultipartFile;

/**
 * Manages files received as a parameter in POST requests to the
 * TranslationController of this REST service.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public class MultipartFileParser {

    /**
     * Parses a multipart file given as a parameter to the POST request into its
     * Properties abstraction.
     * 
     * @param file must be an i18n-compliant .properties file
     * @return properties object representing the content of the MultipartFile
     * @throws IOException in case of non-compliance with .properties structure
     */
    public static Properties parse(MultipartFile file) throws IOException {
	Properties properties = new Properties();

	try (InputStream is = file.getInputStream()) {
	    properties.load(is);
	}

	return properties;
    }
}
