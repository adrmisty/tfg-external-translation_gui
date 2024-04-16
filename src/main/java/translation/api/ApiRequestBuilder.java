package main.java.translation.api;

import java.util.List;
import java.util.Properties;

import com.theokanning.openai.completion.chat.ChatMessage;

/**
 * Used to build the set of relevant translation requests made to the API of a
 * LLM or a translation service, so that the translation of specific contents is
 * carried out.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version April 2024
 */
public interface ApiRequestBuilder {

    /**
     * Builds a set of translation requests for the AI service based on a
     * properties file. These sub-requests are a set of sub-prompts coming from
     * the division of the initial full prompt into smaller ones in order to
     * respect the batch size - that is limited by the max. number of tokens per
     * request.
     * 
     * @param properties file containing localization properties for a program
     * @param targetLang language to which user is translating
     * @return list of chat messages (role: user -> content: prompt of the
     *         translation command)
     * @throws Exception
     */
    public List<ChatMessage> buildRequests(Properties properties,
	    String targetLang);

    public String getModel();

    public int getMaxTokens();

    public double getTemperature();

}
