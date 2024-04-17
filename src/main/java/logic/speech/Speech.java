package main.java.logic.speech;

import java.util.Properties;

import main.java.logic.speech.api.AzureApiSpeech;

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
    public Speech() throws Exception {
	this.apiSpeech = new AzureApiSpeech();
    }

    /**
     * Execution of text to speech functionality for a given language.
     * 
     * @param values list of sentences and texts to read out loud in a given
     *               language
     * @throws InterruptedException
     */
    public void speak(String code, Properties properties)
	    throws InterruptedException {
	apiSpeech.speak(code, properties);
    }

    /**
     * Stops the execution of TTS.
     */
    public void stop() {
	apiSpeech.stop();
    }
}
