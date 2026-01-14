package com.apelisser.base;

import com.apelisser.base.core.application.ApplicationInfoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.TimeZone;

import static java.time.ZoneOffset.UTC;

@SpringBootApplication
@EnableConfigurationProperties({ ApplicationInfoConfig.class })
public class BaseProjectApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(UTC));
        SpringApplication.run(BaseProjectApplication.class, args);
    }

}
