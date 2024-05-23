package main.java.logic.speech;

import java.util.Properties;

import main.java.util.exception.SpeechException;

/**
 * Interface for API access aimed at cognitive speech and text-to-speech
 * functionalities.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public interface ApiSpeech {

    /**
     * Text to speech functionality in a given language, for a set of textual
     * properties.
     * 
     * @param language   identifier, as an alpha-2 code, of a language, either
     *                   region-specific ("en_us") or global ("en")
     * @param properties set of textual key-value pairs to read, where the
     *                   values will be read
     * @throws SpeechException in case of API execution interruption or issues
     *                         performing speech synthesis
     */
    public void speak(String language, Properties properties)
	    throws SpeechException;

    /**
     * Checks whether a given language is supported by the chosen speech API.
     * 
     * In case the specified language to synthesize for is global, and the API
     * only accepts region-specific locale codes, the choice of which
     * accent/regional voice to use will be at random - making the global locale
     * always available, if any of its regional accents also is.
     * 
     * @param language either region-specific ("en_us") or global ("en")
     * @return boolean true if service is available for the given language
     */
    public boolean isAvailableFor(String language);

    /**
     * Stops the voice synthesis/text-to-speech functionaltiy.
     */
    public void stop();

}
