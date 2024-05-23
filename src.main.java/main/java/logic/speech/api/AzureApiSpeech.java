package main.java.logic.speech.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;

import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;

import main.java.logic.speech.ApiSpeech;
import main.java.util.PropertyLoader;
import main.java.util.ResourceLoader;
import main.java.util.exception.ResourceException;

/**
 * API access for text-to-speech functionality provided by Cognitive Speech
 * services.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public class AzureApiSpeech implements ApiSpeech {

    // Voice configuration and speech synthesizer
    private SpeechConfig speech;
    private SpeechSynthesizer synth;

    // Voices supported by the Azure API (all of them region-specific)
    private List<String> availableVoices = new ArrayList<String>();

    public AzureApiSpeech(ResourceBundle messages) throws ResourceException {
	this.speech = SpeechConfig.fromSubscription(
		ResourceLoader.getAzureSpeechApiKey(), "westeurope");
	this.availableVoices = ResourceLoader
		.getSupportedLanguages_Speech(messages);

    }

    @Override
    public void speak(String language, Properties properties) {
	config(language.toLowerCase());
	List<String> values = PropertyLoader.getValues(properties);
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

    @Override
    public boolean isAvailableFor(String language) {
	String lang = language.toLowerCase();
	if (lang.split("-").length == 2
		|| (lang.contains("_") && lang.split("_").length == 2)) {
	    return availableVoices.contains(lang.replace("_", "-"));
	} else {
	    return getAvailableVoice(language) != null;
	}
    }

    /*
     * #########################################################################
     * Auxiliary methods
     * #########################################################################
     */

    // Synthesizer config settings for language
    private void config(String language) {
	language = language.replace("_", "-");
	String[] split = language.split("-");

	if (split.length == 2) { // Global or regional code
	    this.speech.setSpeechSynthesisLanguage(language);
	} else if (split.length == 3) { // Alphabet-specific code
	    this.speech.setSpeechSynthesisLanguage(split[0] + "-" + split[2]);
	} else if (split.length == 1) {
	    this.speech.setSpeechSynthesisLanguage(getAvailableVoice(language));
	}

	this.synth = new SpeechSynthesizer(speech);
    }

    /**
     * This API does not accept global locale codes ("en", "es"), so one is
     * chosen at random from among the region-specific voices supported by the
     * Azure API.
     * 
     * @param global code of format "language" ("en", "es", "pt", "ru"...)
     * @return specific code of format "language-country" if any are found,
     *         otherwise null is returned
     */
    private String getAvailableVoice(String global) {
	global = global.replace("-", "");
	List<String> specs = new ArrayList<>();
	for (String s : availableVoices) {
	    if (!global.equals(s)) {
		String language = s.split("-")[0];
		if (language.equals(global)) {
		    specs.add(s);
		}
	    }
	}

	if (specs.isEmpty()) {
	    return null;
	}
	// One randomly
	return specs.get(new Random().nextInt(specs.size()));
    }

}
