package main.java.translate.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.theokanning.openai.completion.chat.ChatMessage;

import ai.djl.modality.nlp.bert.BertTokenizer;
import main.java.util.ResourceLoader;

/**
 * From a set of properties, this class is used to build the set of relevant
 * requests to be submitted to the API (with a specific command) so that the
 * translation of a given file is carried out.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
public class ApiRequestBuilder {

    public static Properties settings;

    public ApiRequestBuilder() {
	settings = ResourceLoader.getApiSettings();
    }

    /**
     * Builds a set of translation requests for the AI service based on a
     * properties file. These sub-requests are a set of sub-prompts coming from
     * the division of the initial full prompt into smaller ones in order to
     * respect the batch size - that is limited by the max. number of tokens per
     * request.
     * 
     * @param properties: file containing localization properties for a program
     * @param sourceLang: language from which user is translating
     * @param targetLang: language to which user is translating
     * @return list of chat messages (role: user -> content: prompt of the
     *         translation command)
     * @throws Exception
     */
    public List<ChatMessage> buildRequests(Properties properties,
	    String sourceLang, String targetLang) throws Exception {

	String command = String.format("Translate into %s:\n", targetLang);

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
    public String[] buildPrompts(String command, Properties properties) {
	BertTokenizer tokenizer = new BertTokenizer();
	int requestNumber = calculateRequestNumber(tokenizer, command,
		properties);
	String[] prompts = buildSubPrompts(command, properties, requestNumber);
	return prompts;
    }

    /**
     * Builds user messages with the specified sub-prompts.
     * 
     * @param prompts: set of textual prompts with the user's command + text to
     *                 translate
     * @return list of user messages (role, prompt)
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

    private String[] buildSubPrompts(String promptHeader, Properties properties,
	    int requestNumber) {
	String[] subPrompts = new String[requestNumber];
	int batchSize = properties.size() / requestNumber;
	int remainder = properties.size() % requestNumber;

	List<String> sentences = getSentences(properties);
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

    private int calculateRequestNumber(BertTokenizer tokenizer,
	    String promptHeader, Properties properties) {

	int totalTokens = tokenizer.tokenize(promptHeader).size();

	// Calculate the total number of tokens for the given properties
	for (Object property : properties.values()) {
	    totalTokens += tokenizer.tokenize((String) property).size();
	}

	// Calculate the number of requests needed
	int maxTokens = getMaxTokens();
	int requestsNeeded = maxTokens / totalTokens;
	if (totalTokens % maxTokens != 0) {
	    requestsNeeded++; // Round up if there are remaining tokens
	}

	return (requestsNeeded > 3 ? 3 : requestsNeeded);
    }

    private List<String> getSentences(Properties properties) {
	List<String> list = new ArrayList<>();
	for (Object sentence : properties.values()) {
	    list.add((String) sentence);
	}
	return list;
    }

    public int getMaxTokens() {
	return Integer.valueOf(settings.getProperty("_MAX_TOKENS"));
    }

    public String getModel() {
	return settings.getProperty("_ENGINE");
    }

    public double getTemperature() {
	return Double.valueOf(settings.getProperty("_TEMPERATURE"));
    }

}
