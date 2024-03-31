package main.java.speech;

import java.util.List;
import java.util.Locale;

import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;

import main.java.util.exception.LanguageException;
import main.java.util.file.LanguageParser;
import main.java.util.properties.ResourceLoader;

/*
 * Manages text to speech with Windows' Cognitive Speech API. This class allows
 * to read text out loud in a variety of languages and accents, which matches
 * the user's choice in terms of localization.
 * 
 * @author Adriana R.F. (uo282798)
 * @version March 2024
 */
public class Speech {

    // Config for Cognitive Speech
    private SpeechConfig speech;
    // Synthesizer
    private SpeechSynthesizer synth;

    // Localize
    public Speech() throws Exception {
    }

    public void config() throws Exception {
	this.speech = SpeechConfig.fromSubscription(
		ResourceLoader.getSpeechApiKey(), "westeurope");
	this.synth = new SpeechSynthesizer(speech);
    }

    /**
     * Execution of text to speech functionality for a given language.
     * 
     * @param values list of sentences and texts to read out loud in a given
     *               language
     */
    public void speak(List<String> values) {
	for (String s : values) {
	    synth.SpeakText(s);
	}
    }

    /**
     * Sets the language to adapt the speech synthesizer to.
     * 
     * @param locale object containing localization code, i.e. "en-US"
     * @throws LanguageException
     */
    public void setLanguage(Locale locale) throws LanguageException {
	try {
	    this.speech.setSpeechSynthesisLanguage(
		    new LanguageParser().getCode(locale));
	} catch (Exception e) {
	    // Currently, there's support for all the languages this app
	    // supports as well (so this won't really be a potential issue)
	    throw new LanguageException(
		    "ERROR: There is no support for text-to-speech in this language at the moment.",
		    locale);
	}
    }
}
