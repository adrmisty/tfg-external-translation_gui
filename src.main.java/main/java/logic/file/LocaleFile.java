package main.java.logic.file;

import java.util.Locale;
import java.util.Properties;

import main.java.logic.file.locales.LocaleParser;

/**
 * Localization file, abstraction of a .properties key-value pair file
 * saved/to-be-saved in user's storage, in a specific language.
 * 
 * A locale file always exists relative to a file manager, which relates an
 * individual source file to one or more target files.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
public class LocaleFile {

    protected FileManager manager;

    // Default file (code = null)
    private boolean isDefault = false;

    // Language
    private String language;
    private String code;
    private Locale locale;

    // File's property contents
    private Properties content;

    // File path
    private String path;
    private String name;
    private String fullName;

    // Localization bundle
    private String bundleName;

    public LocaleFile(FileManager manager) {
	this.manager = manager;
    }

    /**
     * Includes new content into the source file, as if actually found in the
     * original one.
     * 
     * @param more new info to add to original content, i.e. image descriptions
     */
    public void include(Properties more) {
	this.content.putAll(more);
    }

    public FileManager getManager() {
	return this.manager;
    }

    /**
     * @return whether the file is default or not (alpha-2 code is not shown in
     *         its file path)
     */
    public boolean isDefault() {
	return isDefault;
    }

    /**
     * @param whether the file will be default, true or false
     */
    public void setDefault(boolean isDefault) {
	this.isDefault = isDefault;
    }

    public void setLanguage(String language) {
	LocaleParser parser = manager.getParser();
	this.locale = parser.getLocale(language);
	this.code = parser.getCode(locale);
	this.language = parser.getLanguage(this.code);
    }

    /**
     * @return the display language of the file
     */
    public String getLanguage() {
	return this.language;
    }

    /**
     * @return the file's alpha-2 language code
     */
    public String getCode() {
	if (code == null) {
	    setCode(locale);
	}
	return code;
    }

    /**
     * @param code the alpha-2 code the file will have if null, the default
     *             locale's code is chosen
     */
    public void setCode(String code) {
	LocaleParser parser = manager.getParser();
	if (code == null) {
	    this.locale = Locale.getDefault();
	    this.language = parser.getLanguage(locale);
	    this.code = parser.getCode(this.locale);
	} else {
	    if (code.split("_").length != 2) {
		if (code.contains("_") || code.contains("-")) { // at the end
		    code = code.substring(0, code.length() - 1);
		}
	    }
	    this.language = parser.getLanguage(code);
	    this.locale = new Locale(code);
	    this.code = code;
	}
    }

    /**
     * @param code the alpha-2 code the file will have if null, the default
     *             locale's code is chosen
     */
    public void setCode(Locale locale) {
	LocaleParser parser = manager.getParser();
	this.locale = locale;
	this.language = parser.getLanguage(locale);
	this.code = parser.getCode(locale);
    }

    /**
     * @return the contents of the file
     */
    public Properties getContent() {
	return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(Properties content) {
	this.content = content;
    }

    /**
     * @return the file's path
     */
    public String getPath() {
	return path;
    }

    public void setName(String fullName, String name) {
	this.name = name;
	this.fullName = name; // Contains the code
    }

    /**
     * @return the file's qualified name (default or not)
     */
    public String getName() {
	return name;
    }

    /**
     * @return the file's full name, undefaulted
     */
    public String getFullName() {
	return fullName;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
	this.path = path;
    }

    /**
     * @return the file's bundle name
     */
    public String getBundleName() {
	return bundleName;
    }

    /**
     * @param bundleName the bundle name to set
     */
    public void setBundleName(String bundleName) {
	this.bundleName = bundleName;
    }

}
