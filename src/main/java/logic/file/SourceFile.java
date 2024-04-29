package main.java.logic.file;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import main.java.util.exception.PropertiesException;
import main.java.util.exception.ResourceException;
import main.java.util.properties.ResourceLoader;

/**
 * Abstraction of a source localization file so that its information is readily
 * available and saved for translation (its content, bundle name and format).
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public class SourceFile {

    // Language parser
    private static LocaleParser parser;

    // Is default file
    private boolean isDefault = false;

    // Source file
    private String sourcePath;
    private String sourceLanguage; // Display language ("Bulgarian, Bulgaria")
    private Properties content; // Complete information in file
    private String bundleName; // Bundle name for the app's .properties files

    public SourceFile(ResourceBundle messages) throws ResourceException {
	parser = new LocaleParser(messages);
    }

    /**
     * Checks whether a file is compliant with the i18n format for localization,
     * and retrieves its related information (bundle name + localization codes
     * of the source language it is written in).
     * 
     * @param filepath path to the .properties file
     * @throws PropertiesException in case input file is not i18n compliant
     * @throws IOException
     */
    public void input(String filepath) throws PropertiesException, IOException {

	if (!ResourceLoader.getFileExtension(filepath).get()
		.equals("properties")) {
	    throw new PropertiesException(filepath, false);
	}

	// File path
	this.sourcePath = filepath;

	// Content
	this.content = ResourceLoader.loadProperties(filepath);

	// Bundle name + language code
	String[] sourceName = parser.unformat(filepath);
	this.bundleName = sourceName[0];
	String codes = sourceName[1];

	// No codes!
	// This source file is default
	if (codes == null) {
	    codes = parser.getCode(Locale.getDefault());
	    this.isDefault = true;
	}

	this.sourceLanguage = parser.getLanguage(codes);
    }

    /**
     * Includes new content, as if actually found in the original file (when in
     * fact we are introducing them for the first time).
     * 
     * @param newContent new info to add to original content
     */
    public void include(Properties newContent) {
	this.content.putAll(newContent);
    }

    /*
     * ######################## AUXILIARY METHODS ############################
     */

    /*
     * Resets all source file information.
     */
    public void reset() {
	this.sourceLanguage = "";
	this.content = new Properties();
	this.sourcePath = "";
	this.bundleName = "";
    }

    /**
     * @return the content found in this file
     */
    public Properties getContent() {
	return content;
    }

    /**
     * @return the bundle name for this file
     */
    public String getBundleName() {
	return bundleName;
    }

    public boolean isDefault() {
	return isDefault;
    }

    /**
     * @return the source language of this file (its code!)
     */
    public String getLanguage() {
	return sourceLanguage;
    }

    /**
     * @return locale file parser object
     */
    public LocaleParser getParser() {
	return parser;
    }

    /**
     * @return the source path of this file
     */
    public String getSourcePath() {
	return sourcePath;
    }

}
