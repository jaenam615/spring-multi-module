package com.bigs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

//    @Bean
//    public RestTemplate restTemplate() {
//        // JSON과 XML 응답을 모두 처리할 수 있는 HttpMessageConverter 설정
//        List<HttpMessageConverter<?>> converters = new ArrayList<>();
//
//        // JSON 응답을 처리할 수 있는 변환기 추가
//        converters.add(new MappingJackson2HttpMessageConverter());
//
//        // RestTemplate을 생성하여 커스텀 변환기 설정
//        return new RestTemplate(converters);
//    }
}
