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

    // Config for Cognitive Speech
    private SpeechConfig speech;
    // Synthesizer
    private SpeechSynthesizer synth;
    // Available languages
    private List<String> availableLanguages = new ArrayList<String>();

    public AzureApiSpeech(ResourceBundle messages) throws ResourceException {
	this.speech = SpeechConfig.fromSubscription(
		ResourceLoader.getAzureSpeechApiKey(), "westeurope");
	this.availableLanguages = ResourceLoader
		.getSupportedLanguages_Speech(messages);

    }

    @Override
    public void speak(String language, Properties properties) {
	config(language);
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

    /**
     * @param language, alpha2 code in format "language-country" or "language"
     * @return boolean true whether the language is supported by the Azure API -
     *         in the case of global codes, they are only available if there is
     *         a region-specific code of the same language (as the API only
     *         accepts regional codes)
     */
    @Override
    public boolean isAvailableFor(String language) {
	if (language.split("-").length == 2 || language.contains("_")) {
	    return availableLanguages.contains(language.replace("_", "-"));
	} else {
	    return getRegionalCode(language) != null;
	}
    }

    // Synthesizer config settings for language
    private void config(String language) {
	// azure documentation marks languages with hyphen "-"

	if (language.contains("_")) { // Full, available code
	    this.speech.setSpeechSynthesisLanguage(language.replace("_", "-"));
	} else { // Global code, with no specifics required by the API
	    this.speech.setSpeechSynthesisLanguage(getRegionalCode(language));
	}

	this.synth = new SpeechSynthesizer(speech);
    }

    /**
     * This API does not accept global locale codes, so we will choose one
     * randomly from the specific ones of the same language.
     * 
     * @param global code of format "language"
     * @return specific code of format "language-country" (if any, otherwise,
     *         null)
     */
    private String getRegionalCode(String global) {
	global = global.replace("-", "");
	List<String> specs = new ArrayList<>();
	for (String s : availableLanguages) {
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
