package main.java.logic.speech;

import java.util.Properties;

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
     * @throws InterruptedException in case of API execution interruption
     */
    public void speak(String language, Properties properties)
	    throws InterruptedException;

    /**
     * Stops the execution of TTS.
     */
    public void stop();

}
