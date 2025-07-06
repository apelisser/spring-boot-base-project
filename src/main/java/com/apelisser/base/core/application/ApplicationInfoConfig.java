package com.apelisser.base.core.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties("application.info")
public class ApplicationInfoConfig {

    private final String name;
    private final String description;
    private final String appVersion;
    private final String javaVersion;

    @ConstructorBinding
    public ApplicationInfoConfig(String name, String description, String appVersion, String springBootVersion,
            String javaVersion) {
        this.name = name;
        this.description = description;
        this.appVersion = appVersion;
        this.javaVersion = javaVersion;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    @Override
    public String toString() {
        return "ApplicationInfoConfig{" +
            "name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", appVersion='" + appVersion + '\'' +
            ", javaVersion='" + javaVersion + '\'' +
            '}';
    }

}
