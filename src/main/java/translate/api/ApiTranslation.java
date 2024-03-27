package main.java.translate.api;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import main.java.util.ResourceLoader;

/**
 * Provides access to OpenAI'S ChatCompletions API in order to request the
 * translation of a set of texts found in a .properties file.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public class ApiTranslation {

    private static ApiRequestBuilder apiReq;
    private static OpenAiService service;
    private final static int TIMEOUT = 90;

    public ApiTranslation() {
	service = new OpenAiService(ResourceLoader.getApiKey(),
		Duration.ofSeconds(TIMEOUT));
	apiReq = new ApiRequestBuilder();
    }

    /**
     * Translates a given prompt (containing localization texts that the user
     * has given as input) into a specific language also specified in the prompt
     * via a set of requests to the API.
     * 
     * @param properties:    Properties object containing i18n localization
     *                       settings with texts in a given language
     * @param sourceLanguage (i.e. "English")
     * @param targetLanguage (i.e. "German")
     * @return translated texts as a string
     * 
     * @return properties object with their values translated onto the target
     *         language
     * @throws Exception
     */
    public Properties translate(Properties properties, String sourceLang,
	    String targetLang) throws Exception {

	List<ChatMessage> messages = getRequests(properties, sourceLang,
		targetLang);
	String results = getResults(messages);

	return parseResults(properties, results);
    }

    /**
     * Builds a set of requests to input in the ChatCompletions API.
     * 
     * @param properties: Properties object containing localization texts
     * @param sourceLang: source language that these properties are written in
     * @param targetLang: target language that the user wishes to translate to
     * 
     * @return prompt: string containing all texts to translate
     * @throws Exception in case of empty properties or API error
     */
    private List<ChatMessage> getRequests(Properties properties,
	    String sourceLang, String targetLang) throws Exception {

	List<ChatMessage> messages = new ArrayList<>();

	// Build the respective messages
	if (!properties.isEmpty()) {
	    messages = apiReq.buildRequests(properties, sourceLang, targetLang);
	} else {
	    throw new Exception(
		    "No properties content has been indicated in the specified file.");
	}

	return messages;
    }

    /**
     * Makes a set of requests to the ChatCompletions API.
     * 
     * @param messages: list of chat messages (requests) to input to the API
     * @return results: unified results, as a string, of all these requests
     * 
     */
    private String getResults(List<ChatMessage> messages) {

	StringBuilder results = new StringBuilder();
	int i = 1;
	for (ChatMessage msg : messages) {
	    List<ChatMessage> sub = new ArrayList<>();
	    sub.add(msg);
	    ChatCompletionRequest completionRequest = ChatCompletionRequest
		    .builder().model(apiReq.getModel())
		    .temperature(apiReq.getTemperature())
		    .maxTokens(apiReq.getMaxTokens()).messages(sub).build();
	    ChatCompletionResult completionRes = service
		    .createChatCompletion(completionRequest);

	    results.append(completionRes.getChoices().get(0).getMessage()
		    .getContent());

	    if (i < messages.size()) {
		results.append("\n");
	    }

	    i++;

	}

	// Return translation
	return results.toString();
    }

    /**
     * Parses the results given as a String from the API, as a Properties object
     * according to the format of the object passed as a parameter in the
     * starting query.
     * 
     * @param untranslated properties
     * @param results,     as a complete string
     * 
     * @return replacement of property values for their translations
     */
    private Properties parseResults(Properties properties, String results) {
	String[] res = results.split("\n");
	Properties translations = new Properties();

	Enumeration<Object> keys = properties.keys();
	int i = 0;
	while (keys.hasMoreElements()) {
	    String key = (String) keys.nextElement();
	    translations.put(key, res[i]);
	    i++;
	}

	return translations;
    }
}
