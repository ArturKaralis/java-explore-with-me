package ru.practicum.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;

import org.apache.http.client.HttpClient;

public class ClientConfiguration {
    @Bean
    public HttpClient httpClient() {
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30 * 1000).build();
        return HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
    }
}
