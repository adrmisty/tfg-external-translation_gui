package main.rest.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import jakarta.servlet.MultipartConfigElement;

/**
 * Application configuration properties for the REST service.
 * 
 * @author Adriana R.F. (uo282798@uniovi.es)
 * @version March 2024
 */
@Configuration
public class Config {

    private final static DataSize SIZE = DataSize.parse("5MB");

    /**
     * Handler for multi-part files given as parameter for service translation
     * requests.
     * 
     * @return factory for multi-part element configurations with file/request
     *         sizes specs
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
	MultipartConfigFactory factory = new MultipartConfigFactory();
	factory.setMaxFileSize(SIZE);
	factory.setMaxRequestSize(SIZE);
	return factory.createMultipartConfig();
    }
}
