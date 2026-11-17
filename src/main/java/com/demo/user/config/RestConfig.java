package com.demo.user.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

    // STRICT mapper: unknown / missing fields => exception, so any API contract change is detected
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        ObjectMapper strict = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
        return builder.messageConverters(new MappingJackson2HttpMessageConverter(strict)).build();
    }
}
