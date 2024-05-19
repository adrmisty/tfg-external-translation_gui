package main.java.logic.translation.api.openai;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import com.theokanning.openai.completion.chat.ChatMessage;

import main.java.logic.translation.api.ApiRequestBuilder;
import main.java.util.exception.PropertiesException;
import main.java.util.properties.PropertiesUtil;
import main.java.util.properties.ResourceLoader;

/**
 * Used to build the set of relevant requests to be submitted to the Chat
 * Completions API provided by OpenAi, so that the translation of specific
 * contents is carried out.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version May 2024
 */
public class OpenAIApiRequestBuilder implements ApiRequestBuilder {

    public static Properties settings;

    public OpenAIApiRequestBuilder() throws PropertiesException {
	settings = ResourceLoader.getApiSettings();
    }

    @Override
    public List<ChatMessage> buildRequests(Properties properties,
	    String targetLang) {

	String command = String.format("Translate all sentences to %s:\n",
		targetLang);
	String[] prompts = buildPrompts(command, properties);
	return buildMessages(prompts);
    }

    /**
     * Given a full set of properties and a command (specifying the translation
     * from one language to another), and taking into account the number of
     * tokens to send with each translation in order to receive a full result,
     * the input is divided into smaller sub-prompts.
     * 
     * @param command:    of the format "translate (...) from language A to
     *                    language B"
     * @param properties: full set of i18n properties
     * 
     * @return a list of string prompts
     */
    private String[] buildPrompts(String command, Properties properties) {
	int requestNumber = calculateRequestNumber(command, properties);
	String[] prompts = buildSubPrompts(command, properties, requestNumber);
	return prompts;
    }

    /*
     * Auxiliary functions
     */

    /*
     * Builds user messages with the specified sub-prompts.
     */
    private List<ChatMessage> buildMessages(String[] prompts) {
	List<ChatMessage> userMessage = new ArrayList<>();

	for (int i = 0; i < prompts.length; i++) {
	    ChatMessage msgRole = new ChatMessage();
	    msgRole.setContent(prompts[i]);
	    msgRole.setRole("user");
	    userMessage.add(msgRole);
	}

	return userMessage;
    }

    /*
     * Divides the whole requested text into a set of requests in order to
     * compartmentalize jobs and provide a complete result.
     */
    private String[] buildSubPrompts(String promptHeader, Properties properties,
	    int requestNumber) {
	String[] subPrompts = new String[requestNumber];
	int batchSize = properties.size() / requestNumber;
	int remainder = properties.size() % requestNumber;

	List<String> sentences = PropertiesUtil.getValues(properties);
	List<String> subSentences = new ArrayList<>();
	String prompt = "";
	int index = 0;

	for (int i = 0; i < requestNumber; i++) {
	    prompt = promptHeader;
	    int size = batchSize + (i < remainder ? 1 : 0);

	    subSentences = sentences.subList(index, index + size);
	    prompt += String.join("\n", subSentences);
	    index += size;
	    subPrompts[i] = prompt;
	}

	return subPrompts;
    }

    /*
     * Getters for API setting values (number of max tokens, engine name...)
     */

    private int calculateRequestNumber(String promptHeader,
	    Properties properties) {

	int totalTokens = new StringTokenizer(promptHeader).countTokens();

	// Calculate the total number of tokens for the given properties
	for (Object property : properties.values()) {
	    totalTokens += new StringTokenizer((String) property).countTokens();
	}

	// Calculate the number of requests needed
	int maxTokens = getMaxTokens();
	int requestsNeeded = maxTokens / totalTokens;
	if (totalTokens % maxTokens != 0) {
	    requestsNeeded++; // Round up if there are remaining tokens
	}

	return (requestsNeeded > 1 ? 1 : requestsNeeded);
    }

    @Override
    public String getModel() {
	return String.valueOf(settings.getProperty("_ENGINE"));
    }

    @Override
    public double getTemperature() {
	return Double.valueOf(settings.getProperty("_TEMPERATURE"));
    }

    @Override
    public int getMaxTokens() {
	return Integer.valueOf(settings.getProperty("_MAX_TOKENS"));
    }

}
