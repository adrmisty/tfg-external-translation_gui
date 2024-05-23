package main.java.logic.speech;

import java.util.Properties;
import java.util.ResourceBundle;

import main.java.logic.speech.api.AzureApiSpeech;
import main.java.util.exception.SpeechException;

/**
 * Manages the text-to-speech functionality by accessing a TTS/speech API, for a
 * variety of voices and accents, depending on the required localization.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
public class Speech {

    private ApiSpeech apiSpeech;

    public Speech(ResourceBundle messages) {
	this.apiSpeech = new AzureApiSpeech(messages);
    }

    /**
     * Execution of text to speech functionality for a given language.
     * 
     * @param values list of sentences and texts to read out loud in a given
     *               language
     * @throws SpeechException
     */
    public void speak(String code, Properties properties)
	    throws SpeechException {
	try {
	    apiSpeech.speak(code, properties);
	} catch (Exception e) {
	    // due to thread interruption!
	}
    }

    /**
     * Stops the execution of TTS.
     */
    public void stop() {
	try {
	    apiSpeech.stop();
	} catch (Exception e) {
	    // nothing!
	}
    }

    /**
     * @param language, format "country-code"
     * @return boolean true if service is available for the given language
     */
    public boolean isAvailableFor(String language) {
	return this.apiSpeech.isAvailableFor(language);
    }
}
