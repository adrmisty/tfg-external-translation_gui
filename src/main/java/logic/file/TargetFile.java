package main.java.logic.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.java.logic.util.exception.PropertiesException;
import main.java.logic.util.properties.ResourceLoader;

/**
 * Abstraction of a target localization file where translations will be written.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public class TargetFile {

    // Source
    private SourceFile sourceFile;

    // Settings
    private boolean isDefault = false;

    // Language
    private Locale targetLanguage; // "az-AZ"
    private boolean isAuto;

    // Content in target language
    private Properties content = new Properties();

    // Paths
    private String fileName;
    private String filePath; // Absolute file path to translated file

    // Review temporary file
    private Path temporaryFile;
    private boolean isFileTemporary = false;

    public TargetFile(SourceFile source, String targetLanguage,
	    boolean isDefault, boolean isAuto) {
	this.sourceFile = source;
	this.isDefault = isDefault;
	this.isAuto = isAuto;
	this.targetLanguage = source.getParser().getLocale(targetLanguage);
    }

    /*
     * WRITING
     * ########################################################################
     */

    /**
     * @param content results of translation, to be saved onto target file
     */
    public void setResults(Properties content) {
	if (content == null) {
	    this.content = sourceFile.getContent();
	} else {
	    this.content = content;
	}
    }

    /**
     * Sets directory and file where target file results shall be written.
     * 
     * @param directory where file will be saved
     * @return file path where results will be written to
     */
    public String save(String directory) {
	setFilePath(directory);
	return this.filePath;
    }

    /**
     * Save updates and changes made to temporary file, into definite file.
     * 
     * @param directory where file will eventually be saved
     * @throws IOException         in case of issue with file writing
     * @throws PropertiesException in case of issue updating contents of target
     *                             file
     */
    public void saveReview(String directory)
	    throws PropertiesException, IOException {
	setFilePath(directory);
	Path destination = Paths.get(getFilePath());
	Files.move(temporaryFile, destination);
	this.content = ResourceLoader.loadProperties(getFilePath());
    }

    /*
     * TARGET LANGUAGE
     * ########################################################################
     */

    /**
     * @return display language of target language
     */
    public String getTargetLanguage() {
	return sourceFile.getParser().getLanguage(targetLanguage);
    }

    /**
     * @return alpha-2 code of target language
     */
    public String getTargetCode() {
	return sourceFile.getParser().getCode(targetLanguage);
    }

    /*
     * SETTERS/GETTERS
     * ########################################################################
     */

    /**
     * @param temp new temporary file state
     */
    public void setTemporaryFile(Path file) {
	this.temporaryFile = file;
	this.isFileTemporary = true;
    }

    /**
     * @return path object referencing temporary file
     */
    public Path getTemporaryFile() {
	return temporaryFile;
    }

    /**
     * @return boolean, is file temporary or not
     */
    public boolean isFileTemporary() {
	return isFileTemporary;
    }

    /**
     * @return true/false, whether this translation is a product of automatic
     *         translation or not
     */
    public boolean isFileAuto() {
	return isAuto;
    }

    /**
     * @param path directory path where all target files are saved with a
     *             specific name (bundle name + locale code)
     */
    private void setFilePath(String path) {
	if (path != null) {
	    this.filePath = (path + "/" + getFileName()).replaceAll("\\d", "");
	}

    }

    /**
     * @return localization file name of this target file, which belongs to a
     *         bundle and should have an alpha-2 code (if non-default)
     */
    public String getFileName() {
	if (fileName == null) {
	    this.fileName = setFileName();
	}
	return fileName;
    }

    /**
     * @return content of target file
     */
    public Properties getContent() {
	return content;
    }

    /**
     * @return absolute file path of this target file
     */
    public String getFilePath() {
	if (filePath == null) {
	    setFilePath(null);
	}
	return filePath;
    }

    /**
     * Sets the respective file name for the target file, depending on whether
     * it is a default localization file (without a code) or a localization file
     * with its respective alpha2 code.
     * 
     * @return formatted, localized name for a file belonging to a bundle with
     *         contents written in a specific language
     */
    private String setFileName() {
	String localName;
	if (!isDefault) {
	    localName = sourceFile.getBundleName() + "_"
		    + targetLanguage.toLanguageTag() + ".properties";
	} else {
	    localName = sourceFile.getBundleName() + ".properties";
	}
	return format(localName);
    }

    /**
     * @return content of its source file
     */
    public Properties getSourceContent() {
	return sourceFile.getContent();
    }

    /**
     * Eliminate those instances in locale filenames of the type: "-Latn",
     * "-Cyrl", "-Geor"... Also, replaces all hyphens with underscores so that
     * resulting filename complies with format.
     * 
     * @param filename to remove the possible substring from
     * @return filename without that alphabet indication
     */
    private String format(String filename) {
	Pattern pattern = Pattern.compile("-[A-Za-z]+(?=-[A-Za-z]{2})");
	Matcher matcher = pattern.matcher(filename);

	// Replace matched instances with an empty string
	String result = matcher.replaceAll("");

	return result.replace("-", "_");
    }

}
