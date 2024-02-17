package main.java.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import main.java.utils.ResourceLoader;

public class TranslationApi {

    private static Properties settings;
    private static OpenAiService service;

    public TranslationApi() {
	service = new OpenAiService(ResourceLoader.getApiKey());
	settings = ResourceLoader.getApiSettings();
    }

    /**
     * Translates a given prompt (containing localization texts that the user
     * has given as input) into a specific language also specified in the
     * prompt.
     * 
     * @param messages: user role translation command + localization texts +
     *                  source and target languages
     * 
     * @return localization texts in the target language
     */
    public String translate(List<ChatMessage> messages) {
	ChatCompletionRequest completionRequest = ChatCompletionRequest
		.builder().model(getModel()).temperature(getTemperature())
		.maxTokens(getMaxTokens()).messages(messages).build();

	ChatCompletionResult completionRes = service
		.createChatCompletion(completionRequest);

	// Response cost
	// getResponseCost(completionRes);

	// Return translation
	ChatCompletionChoice result = completionRes.getChoices().get(0);
	return result.getMessage().getContent();
    }

    /**
     * Builds a prompt to input in the ChatCompletions API.
     * 
     * @param properties: Properties object containing localization texts
     * @param sourceLang: source language that these properties are written in
     * @param targetLang: target language that the user wishes to translate to
     * 
     * @return prompt: string containing all texts to translate
     * @throws Exception in case of empty properties or API error
     */
    public List<ChatMessage> setPrompt(Properties properties, String sourceLang,
	    String targetLang) throws Exception {

	List<ChatMessage> messages = new ArrayList<>();

	// Build the respective prompt
	if (!properties.isEmpty()) {
	    messages = buildPrompt(properties, sourceLang, targetLang);
	} else {
	    throw new Exception(
		    "No properties content has been indicated in the specified file.");
	}

	return messages;

    }

    /**
     * The OpenAI API is not free to use, and has a given price depending on the
     * number of tokens used by the application:
     * 
     * https://help.openai.com/en/articles/4936856-what-are-tokens-and-how-
     * to-count-them
     * 
     * Engine used in the project is 'gpt-3.5-turbo': $0.0010 / 1K tokens,
     * information found in https://openai.com/pricing
     * 
     * @param tokens, in request/response
     */
    private double getPrice(long tokens) {
	return Math.round(
		(tokens * getPricing() * Math.pow(10, 4) / Math.pow(10, 4)));
    }

    /**
     * For **debug** purposes, in order to compare pricing for input and output.
     * 
     * @param
     */
    private double getResponseCost(ChatCompletionResult response) {
	long responseTokens = response.getUsage().getCompletionTokens();

	double responsePricing = getPrice(responseTokens);
	System.out
		.println(String.format("# Response cost: %f", responsePricing));
	System.out.println(
		String.format("# Response tokens: %o", responseTokens));

	return responsePricing;
    }

    /**
     * Builds a prompt for the AI service based on a properties file.
     * 
     * @param properties: file containing localization properties for a program
     * @param sourceLang: language from which user is translating
     * @param targetLang: language to which user is translating
     * @return list of chat messages (role: user -> content: prompt of the
     *         translation command)
     * @throws Exception
     */
    private List<ChatMessage> buildPrompt(Properties properties,
	    String sourceLang, String targetLang) throws Exception {
	String command = String.format(
		"Translate the following texts from %s into %s with the same separation format as given",
		sourceLang, targetLang);

	if (properties != null && !properties.isEmpty()) {
	    StringBuilder prompt = new StringBuilder(command + ":\n");

	    for (Object value : properties.values()) {
		prompt.append((String) value + "\n");
	    }

	    checkRequestTokens(prompt.toString());

	    return buildMessages(prompt.toString());
	}
	return null;
    }

    /**
     * Builds user message to send to the service when doing a request.
     * 
     * @param prompt: textual prompt with the user's command + text to translate
     * @return list of user messages (role, prompt)
     */
    private List<ChatMessage> buildMessages(String prompt) {
	List<ChatMessage> userMessage = new ArrayList<>();

	ChatMessage msgRole = new ChatMessage();
	msgRole.setContent(prompt);
	msgRole.setRole("user");

	userMessage.add(msgRole);
	return userMessage;
    }

    /*
     * -------------------- GETTERS
     */

    // Checks whether request tokens surpass a given limit
    private void checkRequestTokens(String prompt) throws Exception {
	int tokens = getTokens(prompt);
	int limit = getRequestTokenLimit();

	if (tokens > limit) {
	    throw new Exception(String.format(
		    "The required number of tokens %d exceed the limit %d. Enter a smaller input.",
		    tokens, limit));
	}
    }

    // Request tokens required for a given prompt
    private int getTokens(String prompt) {
	int requestToken = Math.round(getWords() / prompt.length());
	return requestToken;
    }

    private int getRequestTokenLimit() {
	return Integer
		.valueOf(settings.getProperty("_TOKEN_LIMITS_PER_REQUEST"));
    }

    private int getWords() {
	return Integer.valueOf(settings.getProperty("_WORDS"));
    }

    private int getMaxTokens() {
	return Integer.valueOf(settings.getProperty("_MAX_TOKENS"));
    }

    private String getModel() {
	return settings.getProperty("_ENGINE");
    }

    private double getPricing() {
	return Double.valueOf(settings.getProperty("_PRICE"));
    }

    private double getTemperature() {
	return Double.valueOf(settings.getProperty("_TEMPERATURE"));
    }

}
