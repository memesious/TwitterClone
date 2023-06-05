package com.example.twitterclone.config

import org.keycloak.adapters.KeycloakConfigResolver
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.web.client.RestTemplate

@Configuration
class AppConfig {

    @Autowired
    final KeycloakSpringBootProperties properties

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


    @Bean
    public KeycloakConfigResolver KeycloakConfigResolver() {
        def realm = properties.realm
        return new KeycloakSpringBootConfigResolver();
    }


}
