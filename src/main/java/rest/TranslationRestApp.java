package main.java.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application that posts requests to retrieve translations of
 * .properties i18n files from the OpenAI ChatCompletions API.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
@SpringBootApplication
public class TranslationRestApp {

    public static void main(String[] args) {
	SpringApplication.run(TranslationRestApp.class, args);
    }
}
