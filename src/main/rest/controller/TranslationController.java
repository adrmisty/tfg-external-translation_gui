package main.rest.controller;

import java.io.IOException;
import java.util.Properties;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import main.java.logic.translation.ApiTranslation;
import main.java.logic.translation.api.openai.OpenAIApiTranslation;
import main.rest.util.MultipartFileParser;

/**
 * Controller for REST web service that requests/retrieves translations from
 * another external API, or a database.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
@RestController
public class TranslationController {

    // OpenAI API access (will execute all translations)
    public static ApiTranslation api_rest;

    public TranslationController() throws Exception {
	api_rest = new OpenAIApiTranslation();
    }

    /**
     * Request the translation of a .properties file written in a source
     * language, into a specific target language.
     * 
     * @param file           multi-part, must be an i18n-compliant .properties
     *                       file
     * @param targetLanguage i.e. format "English, United States"/"Spanish"...
     * 
     * @return properties object containing the parsed properties file with the
     *         values being the new translations
     * @throws Exception
     * @throws IOException
     */
    @PostMapping("/translate")
    public Properties translate(@RequestParam("file") MultipartFile file,
	    @RequestParam("targetLanguage") String targetLanguage)
	    throws Exception {

	return api_rest.translate(MultipartFileParser.parse(file),
		targetLanguage);
    }

}
