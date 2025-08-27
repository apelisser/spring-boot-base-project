package com.apelisser.base.core.i18n;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class ResourceMessageConfig {

    /**
     * Creates and configures a ResourceBundleMessageSource bean for message retrieval.
     *
     * @return a ResourceBundleMessageSource bean configured with the specified settings
     */
    @Bean
    ResourceBundleMessageSource messageSource() {
        var source = new ResourceBundleMessageSource();

        // Set the basename of the resource bundle files
        source.setBasenames("i18n/message");

        // Set the base name of the resource bundle files
        source.setDefaultEncoding(StandardCharsets.UTF_8.displayName());

        // Set the location of the properties files containing the messages
        source.setDefaultLocale(Locale.US);

        // true: uses its own code if there is no corresponding message in the file;
        // false: throws no-such-message-exception if there is no corresponding code to the message in the file;
        source.setUseCodeAsDefaultMessage(false);

        return source;
    }

}
