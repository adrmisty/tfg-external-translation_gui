package main.java.speech;

import java.util.List;
import java.util.Locale;
import java.util.Properties;

import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;

import main.java.util.exception.LanguageException;
import main.java.util.file.LanguageParser;
import main.java.util.properties.PropertiesUtil;
import main.java.util.properties.ResourceLoader;

/*
 * Manages text to speech with Windows' Cognitive Speech API. This class allows
 * to read text out loud in a variety of languages and accents, which matches
 * the user's choice in terms of localization.
 * 
 * @author Adriana R.F. (uo282798)
 * @version March 2024
 */
public class Speaker {

    // Config for Cognitive Speech
    private SpeechConfig speech;
    // Synthesizer
    private SpeechSynthesizer synth;

    // Localize
    public Speaker() throws Exception {
	this.speech = SpeechConfig.fromSubscription(
		ResourceLoader.getAzureSpeechApiKey(), "westeurope");
    }

    public void config(String language) throws Exception {
	this.speech.setSpeechSynthesisLanguage(language);
	this.synth = new SpeechSynthesizer(speech);
    }

    /**
     * Execution of text to speech functionality for a given language.
     * 
     * @param values list of sentences and texts to read out loud in a given
     *               language
     * @throws InterruptedException
     */
    public void speak(Properties properties) throws InterruptedException {

	List<String> values = PropertiesUtil.getValues(properties);
	for (String v : values) {
	    synth.SpeakText(v);
	    Thread.sleep(1000);
	}
	stop();
    }

    /**
     * Stops the execution of TTS.
     */
    public void stop() {
	synth.StopSpeakingAsync();
	synth.close();
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
