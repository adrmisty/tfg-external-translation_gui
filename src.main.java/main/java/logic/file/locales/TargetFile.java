package main.java.logic.file.locales;

import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import main.java.logic.file.FileManager;
import main.java.util.resources.FileUtil;
import main.java.util.resources.PropertyLoader;

/**
 * Translation of a source file into a given target language, which can be
 * reviewed and updated by the user.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
public class TargetFile extends LocaleFile {

    // Temporary review
    private Path temporaryPath;

    public TargetFile(FileManager manager, String language, boolean isDefault,
	    String bundle) {
	super(manager);
	setDefault(isDefault);
	setLanguage(language);
	setBundleName(bundle);
    }

    /**
     * @param content results of translation of the source file into the target
     *                language
     */
    @Override
    public void setContent(Properties content) {
	super.setContent(content == null ? manager.getSourceFile().getContent()
		: content);
    }

    /**
     * @param file new temporary file path, for target file review
     */
    public void setTemporaryPath(Path file) {
	this.temporaryPath = file;
    }

    /**
     * @return temporary file path, for target file review
     */
    public Path getTemporaryPath() {
	return this.temporaryPath;
    }

    /**
     * @return boolean, is file temporary or not
     */
    public boolean isTemporary() {
	return this.temporaryPath != null;
    }

    /**
     * @param path directory path where all target files are saved, with a
     *             specific name
     */
    @Override
    public void setPath(String path) {

	String fullName = getBundleName() + "_" + getCode();
	String name;
	if (!this.isDefault()) {
	    name = fullName;
	} else {
	    name = getBundleName();
	}
	// Clean localized target name
	name = FileUtil.format(name);
	super.setName(fullName, name);

	if (path == null) {
	    super.setPath(name + ".properties");
	} else {
	    super.setPath(
		    (path + "/" + name + ".properties").replaceAll("\\d", ""));
	}

    }

    /**
     * Completes the content of a given target file, when its results do not
     * reflect the entirety of keys found in its source file.
     */
    public void complete() {
	List<String> sourceP = PropertyLoader
		.getKeys(manager.getSourceFile().getContent());
	List<String> incomplete = PropertyLoader.getKeys(getContent());

	Properties more = new Properties();
	for (String s : sourceP) {
	    if (!incomplete.contains(s)) {
		getContent().put(s, "");
	    }
	}

	super.include(more);
    }
}
