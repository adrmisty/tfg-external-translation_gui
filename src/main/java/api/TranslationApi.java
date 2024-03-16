package main.java.api;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import main.java.file.ResourceLoader;

public class TranslationApi {

    private static Properties settings;
    private static OpenAiService service;
    private final static int TIMEOUT = 90;

    public TranslationApi() {
	service = new OpenAiService(ResourceLoader.getApiKey(),
		Duration.ofSeconds(TIMEOUT));
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

	List<ChatMessage> subList1 = new ArrayList<>();
	subList1.add(messages.get(0));
	List<ChatMessage> subList2 = new ArrayList<>();
	subList2.add(messages.get(1));

	ChatCompletionRequest completionRequest1 = ChatCompletionRequest
		.builder().model(getModel()).temperature(getTemperature())
		.maxTokens(getMaxTokens()).messages(subList1).build();
	ChatCompletionRequest completionRequest2 = ChatCompletionRequest
		.builder().model(getModel()).temperature(getTemperature())
		.maxTokens(getMaxTokens()).messages(subList2).build();

	ChatCompletionResult completionRes1 = service
		.createChatCompletion(completionRequest1);
	ChatCompletionResult completionRes2 = service
		.createChatCompletion(completionRequest2);

	// Response cost
	// getResponseCost(completionRes);

	// Return translation
	ChatCompletionChoice result1 = completionRes1.getChoices().get(0);
	ChatCompletionChoice result2 = completionRes2.getChoices().get(0);
	return result1.getMessage().getContent()
		.concat("\n" + result2.getMessage().getContent());
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

	int i = 0;
	if (properties != null && !properties.isEmpty()) {
	    StringBuilder prompt1 = new StringBuilder(command + ":\n");
	    StringBuilder prompt2 = new StringBuilder(command + ":\n");

	    for (Object value : properties.values()) {
		if (i < properties.values().size() / 2) {
		    prompt1.append((String) value + "\n");
		} else {
		    prompt2.append((String) value + "\n");
		}
		i++;
	    }

	    return buildMessages(
		    new String[] { prompt1.toString(), prompt2.toString() });
	}
	return null;
    }

    /**
     * Builds user message to send to the service when doing a request.
     * 
     * @param prompt: textual prompt with the user's command + text to translate
     * @return list of user messages (role, prompt)
     */
    private List<ChatMessage> buildMessages(String[] prompts) {
	List<ChatMessage> userMessage = new ArrayList<>();

	ChatMessage msgRole = new ChatMessage();
	msgRole.setContent(prompts[0]);
	msgRole.setRole("user");

	ChatMessage msgRole1 = new ChatMessage();
	msgRole1.setContent(prompts[1]);
	msgRole1.setRole("user");

	userMessage.add(msgRole);
	userMessage.add(msgRole1);

	return userMessage;
    }

    /*
     * -------------------- GETTERS
     */

    private int getMaxTokens() {
	return Integer.valueOf(settings.getProperty("_MAX_TOKENS"));
    }

    private String getModel() {
	return settings.getProperty("_ENGINE");
    }

    private double getTemperature() {
	return Double.valueOf(settings.getProperty("_TEMPERATURE"));
    }

}
