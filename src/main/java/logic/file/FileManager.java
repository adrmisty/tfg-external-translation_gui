package main.java.logic.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import main.java.util.exception.PropertiesException;
import main.java.util.exception.ResourceException;
import main.java.util.properties.PropertiesUtil;

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

    public FileManager(ResourceBundle messages) throws ResourceException {
	sourceFile = new SourceFile(messages);
    }

    /*
     * ######################## PUBLIC METHODS #############################
     */

    /**
     * Retrieves all information found in a source file, if it is i18n-compliant
     * (source language, bundle name and its content).
     * 
     * @param file path of the specified file @ if file cannot be found, there
     *             is an error in processing or the file does not comply with
     *             the desired format
     * @throws IOException
     * @throws PropertiesException
     */
    public void input() throws PropertiesException, IOException {
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
     * @param language  target in format "English, United States"
     * @param isDefault boolean true if this file will be the default one, false
     *                  otherwise
     * @param isAuto    boolean true if this is for automatic translation or not
     * @return new target file for this language @ if specified Locale is not
     *         supported yet
     */
    public TargetFile newLanguage(String language, boolean isDefault,
	    boolean isAuto) {
	TargetFile f = new TargetFile(sourceFile, language, isDefault, isAuto);
	this.targetFiles.add(f);
	return f;
    }

    /**
     * Saves all files (including temporary ones) into the specified directory
     * path.
     * 
     * @param path directory where all files will be saved @ in case of I/O or
     *             API issues
     * @throws IOException
     * @throws PropertiesException
     */
    public void saveAll() throws IOException, PropertiesException {

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
     * @return file path of the translation file in the specific directory @
     * @throws IOException
     */
    public String save(TargetFile f) throws IOException {
	String path = f.save(targetDirectory);
	writeResults(path, f, f.isFileAuto());
	return path;
    }

    /**
     * Returns the absolute path of the manually-translated file (only a
     * possible one).
     * 
     * @return path of manually translated file
     */
    public String getManualPath() {
	return targetFiles.get(0).getFilePath();
    }

    /**
     * Returns the absolute paths of all auto-translated file.
     * 
     * @return list with paths of all translated files
     */
    public List<String> getAutoPaths() {
	List<String> paths = new ArrayList<>();
	for (TargetFile f : targetFiles) {
	    paths.add(f.getFilePath());
	}
	return paths;
    }

    /**
     * For manual translation: upon choosing the directory where to save the
     * manual translation, they are prompted to write their translation onto the
     * new .properties file.
     * 
     * @return file path of the translation file in the specific directory @
     * @throws IOException
     */
    public String manualWrite() throws IOException {
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
     * @param id integer identifier of the target file @ due to IOException or
     *           PropertiesException
     * @throws IOException
     * @throws PropertiesException
     */
    public void saveReview(TargetFile f)
	    throws PropertiesException, IOException {
	f.saveReview(targetDirectory);
    }

    /**
     * For reviewing the translation.
     * 
     * Writes the results into a temporary file which, if the user wants, can be
     * disposed of. Otherwise, they can save the results into a specific
     * directory. This process is done for all files.
     * 
     * @return list of paths to temporary file (so user can open it in an IDE) @
     * @throws IOException
     */
    public List<String> review() throws IOException {
	List<String> paths = new ArrayList<>();
	for (TargetFile f : targetFiles) {
	    paths.add(writeTempResults(f).toAbsolutePath().toString());
	}
	return paths;
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
     * @throws IOException
     */
    private void writeResults(String path, TargetFile f, boolean auto)
	    throws IOException {
	BufferedWriter writer = new BufferedWriter(new FileWriter(path));
	String lang = f.getTargetLanguage();
	Properties pr = f.getContent();

	if (auto) {
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
     * @return Path object representing the temporary file @
     * @throws IOException
     */
    private Path writeTempResults(TargetFile file) throws IOException {

	String fileName = file.getFileName();
	String name = fileName.substring(0, fileName.indexOf("."));

	Path temporaryFile = Files.createTempFile(name, ".properties");
	Files.writeString(temporaryFile, PropertiesUtil
		.getValuesText(file.getTargetLanguage(), file.getContent()));
	file.setTemporaryFile(temporaryFile);

	return temporaryFile;
    }

}
