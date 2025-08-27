package com.apelisser.base;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.apelisser.base.core.application.ApplicationInfoConfig;

@SpringBootApplication
@EnableConfigurationProperties({ ApplicationInfoConfig.class })
public class BaseProjectApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(BaseProjectApplication.class, args);
    }

}
