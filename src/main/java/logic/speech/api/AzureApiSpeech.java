package main.java.logic.speech.api;

import java.util.List;
import java.util.Properties;

import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;

import main.java.logic.speech.ApiSpeech;
import main.java.util.exception.ResourceException;
import main.java.util.properties.PropertiesUtil;
import main.java.util.properties.ResourceLoader;

/**
 * API access for text-to-speech functionality provided by Cognitive Speech
 * services.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public class AzureApiSpeech implements ApiSpeech {

    // Config for Cognitive Speech
    private SpeechConfig speech;
    // Synthesizer
    private SpeechSynthesizer synth;

    public AzureApiSpeech() throws ResourceException {
	this.speech = SpeechConfig.fromSubscription(
		ResourceLoader.getAzureSpeechApiKey(), "westeurope");

    }

    @Override
    public void speak(String language, Properties properties) {
	config(language);
	List<String> values = PropertiesUtil.getValues(properties);
	for (String v : values) {
	    synth.SpeakText(v);
	}
    }

    @Override
    public void stop() {
	try {
	    synth.StopSpeakingAsync();
	} catch (Exception e) {
	    // nothing
	} finally {
	    synth.close();
	}
    }

    // Synthesizer config settings for language
    private void config(String language) {
	// azure documentation marks languages with hyphen "-"
	this.speech.setSpeechSynthesisLanguage(language.replace("_", "-"));

	this.synth = new SpeechSynthesizer(speech);
    }

}
