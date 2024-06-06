package org.instituteatri.deep.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfigTemplate {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}