package main.java.logic.translation.mode;

import java.util.Properties;

import main.java.logic.file.TargetFile;

/**
 * Translation mode, which encompasses the translation process from source file
 * to target file.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public interface TranslationMode {

    /**
     * Translates a source file into a given target language file.
     * 
     * @param target file with a specific target language
     * 
     * @return properties translated to another language
     */
    public Properties translate(TargetFile target);

    /**
     * Resets all associated information in this translation mode.
     */
    public void reset();
}
