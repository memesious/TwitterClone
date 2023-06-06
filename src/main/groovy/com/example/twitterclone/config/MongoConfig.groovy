package com.example.twitterclone.config

import com.mongodb.client.MongoClient
import lombok.RequiredArgsConstructor
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = "com.example.twitterclone.repository")
class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate(MongoClient client, MongoProperties properties) throws Exception {
        return new MongoTemplate(client, properties.database);
    }

}
