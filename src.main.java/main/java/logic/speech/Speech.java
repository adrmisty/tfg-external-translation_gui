package main.java.logic.speech;

import java.util.Properties;
import java.util.ResourceBundle;

import main.java.logic.speech.api.AzureApiSpeech;
import main.java.util.exception.SpeechException;

/**
 * Manages text to speech with Windows' Cognitive Speech API. This class allows
 * to read text out loud in a variety of languages and accents, which matches
 * the user's choice in terms of localization.
 * 
 * @author Adriana R.F. (uo282798)
 * @version March 2024
 */
public class Speech {

    // Azure Cognitive Services API
    private ApiSpeech apiSpeech;

    // Localize
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
