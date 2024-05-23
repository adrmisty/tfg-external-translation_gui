package main.java.logic.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import main.java.logic.file.locales.LocaleParser;
import main.java.logic.file.locales.SourceFile;
import main.java.logic.file.locales.TargetFile;
import main.java.util.PropertyLoader;
import main.java.util.exception.IncompleteResultsException;
import main.java.util.exception.PropertiesException;
import main.java.util.exception.ResourceException;
import main.java.util.exception.ResultsException;

/**
 * Manager for the translation of one source file into one or more languages,
 * and saving those results into user's storage.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
public class FileManager {

    // Locale files
    private LocaleFile sourceFile;
    private List<LocaleFile> targetFiles = new ArrayList<>();
    private String path;

    // Alpha2 codes
    private static LocaleParser parser;

    public FileManager(ResourceBundle messages) throws ResourceException {
	parser = new LocaleParser(messages);
    }

    /**
     * @return the parser used by the file manager to parse and process locale
     *         codes and language names, both in the default and app's specific
     *         locale
     */
    public LocaleParser getParser() {
	return parser;
    }

    /**
     * Processes a source file found in a specified file path.
     * 
     * @param path to process the source file from
     * 
     * @throws IOException         if file cannot be found, there is an error in
     *                             processing or the file does not comply with
     *                             the desired format
     * @throws PropertiesException in case that the file in the specified path
     *                             is not an i18n file with the correct format
     */
    public void input(String path) throws PropertiesException, IOException {
	sourceFile = new SourceFile(this, path);
    }

    /**
     * Creates a new target file for a given language.
     * 
     * @param language  string representing the target language, either global
     *                  "English" or region-specific "English, United States"
     * @param isDefault boolean true if this file will be the default one, false
     *                  otherwise
     * @return new target file for this language @ if specified Locale is not
     *         supported yet
     */
    public TargetFile newLanguage(String language, boolean isDefault) {
	TargetFile f = new TargetFile(this, language, isDefault,
		sourceFile.getBundleName());
	this.targetFiles.add(f);
	return f;
    }

    /**
     * @param path directory where all target files will be saved
     */
    public void to(String path) {
	this.path = path;
	for (LocaleFile f : targetFiles) {
	    f.setPath(path);
	}
    }

    /**
     * Saves all files (including temporary ones) into the specified directory
     * path, depending on the translation mode that has been chosen. *
     * 
     * @return absolute paths of all generated target files
     * 
     * @throws IOException in case of issues writing to file
     */
    public List<String> saveAll() throws IOException {
	ResultsException re = null;

	for (LocaleFile f : targetFiles) {
	    if (!((TargetFile) f).isTemporary()) {
		save(f, f.getContent() == null);
	    } else {
		try {
		    saveReview(f);
		} catch (ResultsException e) {
		    // Go on with execution and handle after
		    // all of them have been validated
		    re = e;
		}
	    }
	}

	if (re != null) {
	    throw re;
	}

	return getResultsPaths();
    }

    /**
     * Returns the absolute paths of all target localization files.
     * 
     * @return list with paths of all translated files
     */
    public List<String> getResultsPaths() {
	List<String> paths = new ArrayList<>();
	for (LocaleFile f : targetFiles) {
	    paths.add(f.getPath());
	}
	return paths;
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
	for (LocaleFile f : targetFiles) {
	    paths.add(writeTemp(f).toAbsolutePath().toString());
	}
	return paths;
    }

    /**
     * @return boolean true if translations have been carried out completely in
     *         each target file
     * @throws IncompleteResultsException
     */
    public boolean areResultsComplete() throws IncompleteResultsException {
	int size = sourceFile.getContent().size();
	boolean complete = true;
	for (LocaleFile f : getTargetFiles()) {
	    if (size != f.getContent().size()) {
		((TargetFile) f).complete();
		complete = false;
	    }
	}

	if (!complete) {
	    throw new IncompleteResultsException();
	}
	return complete;
    }

    /*
     * #########################################################################
     * Auxiliary methods
     * #########################################################################
     */

    public void include(Properties properties) {
	sourceFile.include(properties);
    }

    public String getTargetPath() {
	return this.path;
    }

    public void reset() {
	sourceFile = null;
	targetFiles = new ArrayList<>();
    }

    public boolean isDone() {
	for (LocaleFile f : targetFiles) {
	    if (f.getContent().isEmpty()) {
		return false;
	    }
	}
	return true;
    }

    public List<LocaleFile> getTargetFiles() {
	return targetFiles;
    }

    public LocaleFile getSourceFile() {
	return sourceFile;
    }

    private void saveReview(LocaleFile f)
	    throws PropertiesException, IOException {
	Path destination = Paths.get(f.getPath());
	try {
	    // In case there exists another file with the same name, overwrite
	    // it
	    if (!f.isDefault()) {
		Files.move(((TargetFile) f).getTemporaryPath(), destination,
			StandardCopyOption.REPLACE_EXISTING);
	    } else {
		// If it is default, don't overwrite, and alert the user
		Files.move(((TargetFile) f).getTemporaryPath(), destination);
	    }

	    // Load any possible changes done in the file
	    f.setContent(PropertyLoader.load(f.getPath(), true));
	} catch (IOException e) {
	    // Throw exception: cannot save two default files with the same name
	    // in the same dir
	    // Save it with the code (not default anymore)
	    String path = f.getPath().lastIndexOf("/") + f.getFullName()
		    + ".properties";
	    destination = Paths.get(path);
	    f.setPath(path);
	    f.setDefault(false);
	    Files.move(((TargetFile) f).getTemporaryPath(), destination,
		    StandardCopyOption.REPLACE_EXISTING);

	    // Propagate
	    throw new ResultsException(true, true,
		    String.format(" -> (%s)", f.getCode()));
	}
    }

    private Path writeTemp(LocaleFile f) throws IOException {
	Path temporaryFile = Files.createTempFile(f.getName(), ".properties");
	Files.writeString(temporaryFile,
		PropertyLoader.getValuesText(f.getLanguage(), f.getContent()));
	((TargetFile) f).setTemporaryPath(temporaryFile);
	return temporaryFile;
    }

    private String save(LocaleFile f, boolean auto) throws IOException {
	// Create parent directory if non-existent beforehand
	File file = new File(f.getPath());
	File parentDir = file.getParentFile();
	if (!parentDir.exists()) {
	    parentDir.mkdirs();
	}

	// Create or overwrite the file
	try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
		new FileOutputStream(f.getPath()), "UTF-8"))) {
	    String lang = f.getLanguage();
	    Properties pr = f.getContent();

	    if (auto) {
		writer.write(PropertyLoader.getValuesText(lang, pr).toString());
	    } else {
		writer.write(PropertyLoader.getKeysText(lang, pr).toString());
	    }
	}

	return file.getAbsolutePath();
    }

}
