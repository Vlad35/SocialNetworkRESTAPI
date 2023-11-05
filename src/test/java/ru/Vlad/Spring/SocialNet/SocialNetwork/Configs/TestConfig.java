package ru.Vlad.Spring.SocialNet.SocialNetwork.Configs;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
public class TestConfig {

    @Bean
    public TestRestTemplate testRestTemplate() {
        return new TestRestTemplate();
    }
}
