package main.java.api;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import main.java.utils.I18NFileParser;
import main.java.utils.LocaleParser;

/**
 * Provides access to OpenAI's ChatCompletions API, which will be used for
 * translating a given .properties file.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version 1.0 (February 2024)
 */
public class Translator {

    private static TranslationApi api;

    // Parsed information from file
    private String filePath = "";
    private Properties properties;

    // Translation process
    private String sourceLanguage = ""; // "English, United States"
    private String targetLanguage = ""; // "German, Germany"
    private String targetCode = ""; // "de_DE"
    private String bundleName = "";
    private String results = "";

    public Translator() {
	api = new TranslationApi();
    }

    /**
     * Inputs the file that the user wishes to translate, which must be
     * compliant with i18n format. If not, an exception is thrown.
     * 
     * @param path to the file
     * @throws exception if file is not format-compliant
     */
    public void input(String path) throws Exception {
	Map<String, Object> parsed = I18NFileParser.parse(filePath);

	// If no error happens...
	saveAllFileInfo(parsed);
	this.filePath = path;

    }

    /**
     * Executes the translation process, from a given input filePath: -
     * establishes target language - accesses API to build the specific prompt -
     * retrieves result as a String
     * 
     * @param targetLang, target language (format: "English, United States")
     * @return translated texts as a string
     * @throws Exception
     */
    public String translateTo(String target) throws Exception {
	saveTargetLanguage(target);
	String texts = translateThruApi(this.properties, this.sourceLanguage,
		this.targetLanguage);
	this.results = texts;

	return texts;
    }

    /**
     * Accesses API to translate a set of properties from a source language to a
     * target language.
     * 
     * @param properties
     * @param sourceLanguage (i.e. "English")
     * @param targetLanguage (i.e. "German"
     * @return translated texts
     * @throws Exception
     */
    public String translateThruApi(Properties properties, String sourceLang,
	    String targetLang) throws Exception {
	String prompt = api.setPrompt(properties, sourceLang, targetLang);
	return api.translate(prompt);
    }

    private void saveTargetLanguage(String targetLanguage) throws Exception {
	Locale target = LocaleParser.extract(targetLanguage);
	this.targetLanguage = target.getDisplayLanguage();
	this.targetCode = target.toString();
    }

    private void saveAllFileInfo(Map<String, Object> parsed) {
	this.properties = (Properties) parsed.get("properties");

	@SuppressWarnings("unchecked")
	Map<String, String> fileInfo = (Map<String, String>) parsed
		.get("fileInfo");
	this.sourceLanguage = LocaleParser.getLanguage(fileInfo.get("locales"));
	this.bundleName = fileInfo.get("bundleName");
    }

}
