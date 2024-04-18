package main.java.logic.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import main.java.logic.util.properties.PropertiesUtil;

/**
 * Manager for the translation of one source file into 1 or more target files.
 * 
 * Contains relevant information to the file that is currently being processed
 * by the application (properties contents, original bundle, and source/target
 * languages).
 * 
 * Deals with writing respective results to target files.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public class FileManager {

    // File processor: processes source file
    private String sourcePath;
    private SourceFile sourceFile;

    // Target files
    private String targetDirectory;
    private List<TargetFile> targetFiles = new ArrayList<>();

    public FileManager(ResourceBundle messages) throws Exception {
	sourceFile = new SourceFile(messages);
    }

    /*
     * ######################## PUBLIC METHODS #############################
     */

    /**
     * Retrieves all information found in a source file, if it is i18n-compliant
     * (source language, bundle name and its content).
     * 
     * @param file path of the specified file
     * @throws Exception if file cannot be found, there is an error in
     *                   processing or the file does not comply with the desired
     *                   format
     */
    public void input() throws Exception {
	sourceFile.input(sourcePath);
    }

    /**
     * @param path absolute file path from which source file is read
     */
    public void from(String path) {
	this.sourcePath = path;
    }

    /**
     * @param directory target directory where all files will be saved
     */
    public void to(String directory) {
	this.targetDirectory = directory;
    }

    /**
     * Establishes new target translation file.
     * 
     * @param language target in format "English, United States"
     * @return new target file for this language
     * @throws Exception if specified Locale is not supported yet
     */
    public TargetFile newLanguage(String language) throws Exception {
	TargetFile f = new TargetFile(sourceFile, language);
	this.targetFiles.add(f);
	return f;
    }

    /**
     * Saves all files (including temporary ones) into the specified directory
     * path.
     * 
     * @param path directory where all files will be saved
     * @throws Exception in case of I/O or API issues
     */
    public void saveAll() throws Exception {

	for (TargetFile f : targetFiles) {
	    if (!f.isFileTemporary()) {
		save(f);
	    } else {
		saveReview(f);
	    }

	}

    }

    /**
     * Writes translated properties into a target file, in its respective target
     * language.
     * 
     * @param f target file
     * 
     * @return file path of the translation file in the specific directory
     * @throws Exception
     */
    public String save(TargetFile f) throws Exception {
	String path = f.save(targetDirectory);
	writeResults(path, f, false);
	return path;
    }

    /**
     * For manual translation: upon choosing the directory where to save the
     * manual translation, they are prompted to write their translation onto the
     * new .properties file.
     * 
     * @return file path of the translation file in the specific directory
     * @throws Exception
     */
    public String manualWrite() throws Exception {
	TargetFile f = targetFiles.get(0);
	String path = f.save(targetDirectory);
	writeResults(path, f, true);
	return f.getFilePath();
    }

    /**
     * @return source file this translation manager works around
     */
    public SourceFile getSourceFile() {
	return sourceFile;
    }

    /**
     * From the temporary file that was used during reviewing, save these
     * results onto the specified directory.
     * 
     * @param id integer identifier of the target file
     * @throws Exception due to IOException or PropertiesException
     */
    public void saveReview(TargetFile f) throws Exception {
	f.saveReview(targetDirectory);
    }

    /**
     * For reviewing the translation.
     * 
     * Writes the results into a temporary file which, if the user wants, can be
     * disposed of. Otherwise, they can save the results into a specific
     * directory.
     * 
     * @param id integer identifier of target file to review
     * @return path to temporary file (so user can open it in an IDE)
     * @throws Exception
     */
    public String review(int id) throws Exception {
	return writeTempResults(targetFiles.get(id)).toAbsolutePath()
		.toString();
    }

    /**
     * Includes a set of properties, as if found in the original file (when in
     * fact we are introducing them for the first time).
     * 
     * -> To be used in combination with image captioning.
     * 
     * @param properties new info to add to "original" file
     */
    public void include(Properties properties) {
	sourceFile.include(properties);
    }

    /*
     * SETTERS & GETTERS
     * ########################################################################
     */

    /**
     * Resets all file information.
     */
    public void reset() {
	sourceFile.reset();
	targetFiles = new ArrayList<>();
    }

    /**
     * @return directory path where files will be saved
     */
    public String getTargetDirectory() {
	return targetDirectory;
    }

    /**
     * @return true if translation process is over, false otherwise
     */
    public boolean isDone() {
	for (TargetFile f : targetFiles) {
	    if (f.getContent().isEmpty()) {
		return false;
	    }
	}
	return true;
    }

    /*
     * ######################## WRITING METHODS #############################
     */

    /**
     * From a given set of translated sentences (that represent the property
     * values of an input property file in another language), they are parsed
     * and written to a given target file as "{property_name}={property_text}".
     * 
     * @param path       where results will be written
     * @param properties textual properties to parse
     * @param language   target display language
     * @param boolean    keys true if only keys must be written, false otherwise
     * @throws Exception in case of I/O exception
     */
    private void writeResults(String path, TargetFile f, boolean keys)
	    throws Exception {
	BufferedWriter writer = new BufferedWriter(new FileWriter(path));
	String lang = f.getTargetLanguage();
	Properties pr = f.getContent();

	if (keys) {
	    writer.write(PropertiesUtil.getValuesText(lang, pr).toString());
	} else {
	    writer.write(PropertiesUtil.getKeysText(lang, pr).toString());
	}
	writer.close();
    }

    /**
     * Writes results onto a temporary file, to be used for reviewing.
     * 
     * @param results textual properties to parse
     * @param file    target file
     * @return Path object representing the temporary file
     * @throws Exception
     */
    private Path writeTempResults(TargetFile file) throws Exception {

	String fileName = file.getFileName();
	String name = fileName.substring(0, fileName.indexOf("."));

	Path temporaryFile = Files.createTempFile(name, ".properties");
	Files.writeString(temporaryFile, PropertiesUtil
		.getValuesText(file.getTargetLanguage(), file.getContent()));
	file.setTemporaryFile(temporaryFile);

	return temporaryFile;
    }

}
