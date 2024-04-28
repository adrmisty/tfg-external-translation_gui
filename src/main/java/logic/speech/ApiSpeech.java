package main.java.logic.speech;

import java.util.Properties;

import main.java.util.exception.SpeechException;

/**
 * Interface for API access aimed at cognitive speech (text-to-speech
 * functionality).
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public interface ApiSpeech {

    /**
     * Text to speech functionality in a given language, for a set of textual
     * properties.
     * 
     * @param code       identifier, as an alpha-2 code, of a language
     * @param properties set of textual properties to read
     * @throws SpeechException in case of API execution interruption
     */
    public void speak(String language, Properties properties)
	    throws SpeechException;

    /**
     * Stops the execution of TTS.
     */
    public void stop();

    /**
     * Checks whether a given language is supported by the chosen speech API.
     * 
     * @param language, format "country-code"
     * @return boolean true if service is available for the given language
     */
    public boolean isAvailableFor(String language);

}
