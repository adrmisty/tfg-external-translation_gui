package main.java.api;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import com.theokanning.openai.completion.chat.ChatMessage;

import main.java.utils.LocaleFile;
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
    private static Map<String, Locale> map;

    // Parsed information from file
    private Properties properties;
    private String filePath = "";
    private String sourceLanguage = ""; // "English, United States"
    private String bundleName = "";

    // Translation process
    private String targetLanguage = ""; // "lGerman, Germany"
    private String targetCode = ""; // "de_DE"
    private String results = "";

    public Translator() {
	api = new TranslationApi();
	map = LocaleParser.getMap();
	reset();
    }

    public void reset() {
	this.filePath = "";
	this.properties = null;
	this.sourceLanguage = ""; // "English, United States"
	this.targetLanguage = ""; // "German, Germany"
	this.targetCode = ""; // "de_DE"
	this.bundleName = "";
	this.results = "";
    }

    public boolean isDone() {
	return !results.isBlank();
    }

    /**
     * Inputs the file that the user wishes to translate, which must be
     * compliant with i18n format. If not, an exception is thrown.
     * 
     * @param path to the file
     * @throws exception if file is not format-compliant
     */
    public void input(String path) throws Exception {
	Map<String, Object> parsed = LocaleFile.parse(path);

	// If no error happens...
	saveAllFileInfo(parsed);
	this.filePath = path;
    }

    public String getSavedFileName() {
	return bundleName + "_" + targetCode + ".properties";
    }

    /**
     * Writes results into chosen file path.
     * 
     * @param filepath (absolute directory path where file should be saved)
     * @throws IOException
     */
    public void save(String filepath) throws IOException {
	LocaleFile.write(filepath, bundleName, targetCode, results,
		properties.keys());
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
     * @param properties:    Properties object containing i18n localization
     *                       settings with texts in a given language
     * @param sourceLanguage (i.e. "English")
     * @param targetLanguage (i.e. "German")
     * @return translated texts as a string
     * @throws Exception
     */
    private String translateThruApi(Properties properties, String sourceLang,
	    String targetLang) throws Exception {
	List<ChatMessage> messages = api.setPrompt(properties, sourceLang,
		targetLang);
	return api.translate(messages);
    }

    /**
     * Saves the chosen target language (country-specific) both as a tag/code
     * and as the display name of the language.
     * 
     * @param targetLanguage, format: "English, United States"
     * @throws Exception
     */
    private void saveTargetLanguage(String targetLanguage) throws Exception {
	Locale target = LocaleParser.extract(map, targetLanguage);
	this.targetLanguage = target.getDisplayLanguage();
	this.targetCode = target.getLanguage() + "-" + target.getCountry();
    }

    /**
     * From a dictionary-like object, saves all info for further procressing:
     * properties in file, source language, bundle name of the file.
     * 
     * @param Map<String,Object> parsed, information parsed from file
     */
    private void saveAllFileInfo(Map<String, Object> parsed) {
	this.properties = (Properties) parsed.get("properties");

	@SuppressWarnings("unchecked")
	Map<String, String> fileInfo = (Map<String, String>) parsed
		.get("fileInfo");
	this.sourceLanguage = LocaleParser.getLanguage(fileInfo.get("locales"));
	this.bundleName = fileInfo.get("bundleName");
    }

}
