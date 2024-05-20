package main.java.logic.translation.api.openai;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import main.java.gui.util.ExceptionHandler;
import main.java.logic.translation.api.ApiRequestBuilder;
import main.java.logic.translation.api.ApiTranslation;
import main.java.util.exception.PropertiesException;
import main.java.util.exception.ResourceException;
import main.java.util.exception.TranslationException;
import main.java.util.properties.PropertiesUtil;
import main.java.util.properties.ResourceLoader;

/**
 * Provides access to OpenAI'S ChatCompletions API in order to request the
 * translation of a set of texts found in a .properties file.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
public class OpenAIApiTranslation implements ApiTranslation {

    // ApiTranslation interface
    private ApiTranslation apiInterface;

    // Translation resultzs
    private Properties results;

    private static ApiRequestBuilder apiReq;
    private static OpenAiService service;
    private final static int TIMEOUT = 90;

    // Error codes
    private final static String QUOTA_REACHED = "insufficient_quota";
    private final static String RATE_LIMIT = "rate_limit_error";

    /**
     * Creates an ApiTranslation object aimed to manage requests to the
     * ChatCompletionsAPI.
     * 
     * @throws PropertiesException (extending ResourceException) in case api
     *                             settings provided in program properties are
     *                             not valid
     * 
     * @throws ResourceException   in case API key necessary for OpenAI use is
     *                             not found
     */
    public OpenAIApiTranslation() throws ResourceException {
	service = new OpenAiService(ResourceLoader.getApiKey(),
		Duration.ofSeconds(TIMEOUT));
	apiReq = new OpenAIApiRequestBuilder();
    }

    // Mock testing
    public OpenAIApiTranslation(ApiTranslation api) {
	this();
	this.apiInterface = api;
    }

    @Override
    public Properties translate(Properties properties, String sourceLang,
	    String targetLang) throws TranslationException {

	if (apiInterface != null) {
	    return apiInterface.translate(properties, sourceLang, targetLang);
	}

	return getApiResults(properties, sourceLang, targetLang);
    }

    @Override
    public Properties getResults() {
	return this.results;
    }

    /*
     * ######################## AUXILIARY METHODS ##############################
     */

    private Properties getApiResults(Properties properties, String sourceLang,
	    String targetLang) throws TranslationException {
	List<ChatMessage> messages = getRequests(properties, sourceLang,
		targetLang);
	try {
	    this.results = PropertiesUtil.replaceValues(properties,
		    getResults(messages));
	    return this.results;
	} catch (OpenAiHttpException e) {
	    // Try again in 30s if rate limit has been reached
	    if (isError(e, RATE_LIMIT)) {

		try {
		    Thread.sleep(3000);
		    translate(properties, sourceLang, targetLang);
		} catch (Exception e2) {
		    // Nothing
		}
	    } else if (isError(e, QUOTA_REACHED)) {
		// Show error message and terminate application
		ExceptionHandler.handle(null, e, true);
	    }
	} catch (Exception e) {
	    // Retry
	    try {
		Thread.sleep(3000);
		translate(properties, sourceLang, targetLang);
	    } catch (Exception e2) {
		// Nothing
	    }
	}
	return this.results;

    }

    /**
     * Builds a set of requests to input in the ChatCompletions API.
     * 
     * @param properties Properties object containing localization texts
     * @param sourceLang source language the original content is in
     * @param targetLang target language that the user wishes to translate to
     * 
     * @return prompt: string containing all texts to translate @ in case of
     *         empty properties or API error
     */
    private List<ChatMessage> getRequests(Properties properties,
	    String sourceLang, String targetLang) throws TranslationException {

	List<ChatMessage> messages = new ArrayList<>();

	// Build the respective messages
	if (!properties.isEmpty()) {
	    messages = apiReq.buildRequests(properties, sourceLang, targetLang);
	} else {
	    throw new TranslationException();
	}

	return messages;
    }

    /**
     * Parses results from a set of messages to request to the API.
     * 
     * @param messages list of chat messages (requests) to input to the API
     * @return results unified results, as a string, of all these requests
     * @throws InterruptedException
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

	    // In case rate limit is reached, try again!
	    ChatCompletionResult completionRes = service
		    .createChatCompletion(completionRequest);
	    results.append(completionRes.getChoices().get(0).getMessage()
		    .getContent().replace("-", ""));

	    if (i < messages.size()) {
		results.append("\n");
	    }
	    i++;
	}

	// Return translation
	return results.toString();
    }

    // Error codes checking

    private boolean isError(OpenAiHttpException e, String code) {
	return e.code.equals(code);

    }

}
