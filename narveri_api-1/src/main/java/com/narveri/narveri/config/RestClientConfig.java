package com.narveri.narveri.config;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestTemplateAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.Duration;

@Configuration
@ComponentScan
public class RestClientConfig {

//    @Value("${esnek_pos.url}")
//    private String esnek_pos_url;
//
//
//
//    @Bean
//    EsnekPosService esnekPosServiceRestTemplate(RestTemplateBuilder restTemplateBuilder) {
//        RestTemplate restTemplate = restTemplateBuilder
//                .uriTemplateHandler(new DefaultUriBuilderFactory(esnek_pos_url))
//                .setConnectTimeout(Duration.ofSeconds(10))
//                .setReadTimeout(Duration.ofSeconds(40))
//
//                .additionalInterceptors(new RestTemplateInterceptor())
//                .build();
//        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory()));
//        RestTemplateAdapter adapter = RestTemplateAdapter.create(restTemplate);
//        return HttpServiceProxyFactory.builderFor(adapter).build().createClient(EsnekPosService.class);
//
//    }

}