package com.mkr.app.consumer.httpinterface;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpInterfaceConfig {

    @Primary
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }


    @Bean
    public ProviderHttpInterface webClientHttpInterface(WebClient.Builder webClientBuilder) {
        WebClient webClient = webClientBuilder.baseUrl("http://provider").build(); // http://localhost:8081
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        ProviderHttpInterface service = factory.createClient(ProviderHttpInterface.class);
        return service;
    }

//    @Bean
//    @LoadBalanced
//    public RestClient.Builder restClientBuilder() {
//        return RestClient.builder();
//    }

//    @Bean
//    public ProviderHttpInterface restClientHttpInterface(RestClient.Builder restClientBuilder) {
//        RestClient restClient = restClientBuilder.baseUrl("http://provider").build();
//        RestClientAdapter adapter = RestClientAdapter.create(restClient);
//        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
//
//        ProviderHttpInterface service = factory.createClient(ProviderHttpInterface.class);
//        return service;
//    }

//    @Bean
//    @LoadBalanced
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }

//    @Bean
//    public ProviderHttpInterface restTemplateHttpInterface(RestTemplate restTemplate) {
//        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://provider"));
//        RestTemplateAdapter adapter = RestTemplateAdapter.create(restTemplate);
//        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
//
//        ProviderHttpInterface service = factory.createClient(ProviderHttpInterface.class);
//        return service;
//    }
}
