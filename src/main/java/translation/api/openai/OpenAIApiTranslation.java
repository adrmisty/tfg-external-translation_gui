package main.java.translation.api.openai;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import main.java.translation.api.ApiRequestBuilder;
import main.java.translation.api.ApiTranslation;
import main.java.util.exception.PropertiesException;
import main.java.util.exception.TranslationException;
import main.java.util.properties.PropertiesUtil;
import main.java.util.properties.ResourceLoader;

/**
 * Provides access to OpenAI'S ChatCompletions API in order to request the
 * translation of a set of texts found in a .properties file.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public class OpenAIApiTranslation implements ApiTranslation {

    // Translation results
    private Properties results;

    private static ApiRequestBuilder apiReq;
    private static OpenAiService service;
    private final static int TIMEOUT = 90;

    /**
     * Creates an ApiTranslation object aimed to manage requests to the
     * ChatCompletionsAPI.
     * 
     * @throws PropertiesException
     */
    public OpenAIApiTranslation() throws Exception {
	service = new OpenAiService(ResourceLoader.getApiKey(),
		Duration.ofSeconds(TIMEOUT));
	apiReq = new OpenAIApiRequestBuilder();
    }

    @Override
    public Properties translate(Properties properties, String targetLang)
	    throws TranslationException {
	List<ChatMessage> messages = getRequests(properties, targetLang);
	this.results = PropertiesUtil.replaceValues(properties,
		getResults(messages));
	return this.results;
    }

    @Override
    public Properties getResults() {
	return this.results;
    }

    /*
     * ######################## AUXILIARY METHODS ##############################
     */

    /**
     * Builds a set of requests to input in the ChatCompletions API.
     * 
     * @param properties Properties object containing localization texts
     * @param targetLang target language that the user wishes to translate to
     * 
     * @return prompt: string containing all texts to translate
     * @throws Exception in case of empty properties or API error
     */
    private List<ChatMessage> getRequests(Properties properties,
	    String targetLang) throws TranslationException {

	List<ChatMessage> messages = new ArrayList<>();

	// Build the respective messages
	if (!properties.isEmpty()) {
	    messages = apiReq.buildRequests(properties, targetLang);
	} else {
	    throw new TranslationException(
		    "No properties content has been indicated in the specified file.");
	}

	return messages;
    }

    /**
     * Parses results from a set of messages to request to the API.
     * 
     * @param messages list of chat messages (requests) to input to the API
     * @return results unified results, as a string, of all these requests
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

}
